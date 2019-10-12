package fr.missoum.utils.io.readers

import java.io.File

import fr.missoum.logic.{Blob, EntryTree}
import fr.missoum.utils.helpers.PathHelper

object WorkspaceReaderImpl extends WorkspaceReader {
  def fileExists(path: String): Boolean = scala.reflect.io.File(path).exists

  def recursiveListFiles(f: File): Array[File] = {
    if (!f.isDirectory)
      Array()
    val files = f.listFiles.filter(x => x.isFile)
    val directories = f.listFiles.filter(x => x.isDirectory && !x.getName.startsWith(PathHelper.SgitRepositoryName))
    files ++ directories.flatMap(recursiveListFiles(_))
  }

  def getAllBlobsOfWorkspace(): Array[EntryTree] = {
    val workspaceFiles = recursiveListFiles(new File(PathHelper.SgitPath))
    val result = workspaceFiles.map(x => {
      val content = SgitReaderImpl.getContentOfFile(x.getAbsolutePath)
      Blob.newBlobWithContent(content, PathHelper.getSimplePathOfFile(x.getAbsolutePath))
    })
    result
  }
}
