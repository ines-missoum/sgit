package fr.missoum.utils.io.workspace

import java.io.{File, PrintWriter}

import fr.missoum.logic.{Blob, EntryTree}
import fr.missoum.utils.helpers.PathHelper
import fr.missoum.utils.io.readers.{SgitReader, SgitReaderImpl}

import scala.io.Source

object WorkspaceManagerImpl extends WorkspaceManager {

  var reader: SgitReader = SgitReaderImpl

  def fileExists(path: String): Boolean = scala.reflect.io.File(path).exists

  def recursiveListFiles(f: File): List[File] = {
    if (!f.isDirectory)
      List()
    val files = f.listFiles.filter(x => x.isFile).toList
    val directories = f.listFiles.filter(x => x.isDirectory && !x.getName.startsWith(PathHelper.SgitRepositoryName))
    files ++ directories.flatMap(recursiveListFiles(_))
  }

  def getAllBlobsOfWorkspace(): List[EntryTree] = {
    val workspaceFiles = recursiveListFiles(new File(PathHelper.SgitPath))
    val result = workspaceFiles.map(x => {
      val content = getContentOfFile(x.getAbsolutePath).mkString("\n")
      Blob.newBlobWithContent(content, PathHelper.getSimplePathOfFile(x.getAbsolutePath))
    })
    result
  }

  /**
   * From the lists of blobs that sould be deleted and created, it's updating the workspace content
   *
   * @param toDelete blobs to delete from the workspace
   * @param toCreate blobs to create in the workspace
   */
  def updateWorkspace(toDelete: List[EntryTree], toCreate: List[EntryTree]): Unit = {
    deleteRec(toDelete)
    createRec(toCreate)
  }

  /**
   * Deletes all empty directories from the directory in parameter
   *
   * @param dir directory
   */
  private def cleanRecEmptyDir(dir: File): Unit = {
    //if not root and empty
    if (!dir.getAbsolutePath.startsWith(PathHelper.SgitPath) && dir.listFiles().isEmpty) {
      dir.delete()
      cleanRecEmptyDir(dir.getParentFile)
    }

  }

  /**
   * Deletes recursively all the files that corresponds to blobs in parameter
   *
   * @param toDelete list of blobs
   */
  private def deleteRec(toDelete: List[EntryTree]): Unit = {
    if (toDelete.nonEmpty) {
      val file = new File(PathHelper.getAbsolutePathOfFile(toDelete(0).path))
      if (file.exists()) file.delete()
      cleanRecEmptyDir(file.getParentFile)
      deleteRec(toDelete.tail)
    }
  }

  /**
   * Creates recursively all the files that corresponds to blobs in parameter
   *
   * @param toCreate list of blobs
   */
  private def createRec(toCreate: List[EntryTree]): Unit = {
    if (toCreate.nonEmpty) {
      val pathFile = PathHelper.getAbsolutePathOfFile(toCreate(0).path)
      val dirPath = pathFile
        .split(File.separator)
        .dropRight(1)
        .mkString(File.separator)
      val file = new File(pathFile)
      val dir = new File(dirPath)
      dir.mkdirs()
      file.createNewFile()
      //we write the content in the file
      new PrintWriter(pathFile) {
        val content = reader.getContentOfObjectInString(toCreate(0).hash)
        if (content.nonEmpty)
          write(content.get)
        close()
      }
      createRec(toCreate.tail)
    }
  }

  def getContentOfFile(absPath: String): List[String] = {
    val source = Source.fromFile(absPath)
    val content = source.getLines.toList
    source.close()
    content
  }

}
