package fr.missoum.commands

import java.io.File

import fr.missoum.logic.{Blob, EntryTree}
import fr.missoum.utils.helpers.{HashHelper, PathHelper}
import fr.missoum.utils.io.readers.{SgitReaderImpl, WorkspaceReaderImpl}
import fr.missoum.utils.io.writers.SgitWriterImpl

import scala.annotation.tailrec

object SgitAddImpl extends SgitAdd{

  def isNotInWorkspace(fileName: String): Boolean =
    !WorkspaceReaderImpl.fileExists(System.getProperty("user.dir") + File.separator + fileName)

  def isNotExistingFile(fileName: String): Boolean = {
    val indexBlobsAbsolutePaths = SgitReaderImpl.getIndex().map(x => Blob(x)).map(x => PathHelper.getAbsolutePathOfFile(x.path))
    val absolutePath = System.getProperty("user.dir") + File.separator + fileName
    val isExistInWorkspace = WorkspaceReaderImpl.fileExists(absolutePath)
    val isExistingInIndex = indexBlobsAbsolutePaths.exists(_.equals(absolutePath))
    // do not exists either in workspace nor in index
    !isExistInWorkspace && !isExistingInIndex
  }

  def getNotExistingFile(filesNames: Array[String]) =
    filesNames.filter(x => isNotExistingFile(x))

  /**
   *
   * @param filesNames : pre: all the files exist in index
   */
  def addAll(filesNames: Array[String]): Unit = {

    // we retrieve all the content of the index file in format of array of blobs
    val indexBlobs = SgitReaderImpl.getIndex().map(x => Blob(x))
    //we create an array of blobs from the array of files names:

    //files to delete <=> files that don't exist but that are contain in the index
    val blobsToRemove = filesNames.filter(isNotInWorkspace(_)).map(x => {
      val absolutePath = System.getProperty("user.dir") + File.separator + x
      val simplePath = PathHelper.getSimplePathOfFile(absolutePath)
      Blob("", simplePath)
    })

    //files to add or update
    val newFilesBlobs: Array[EntryTree] = filesNames.filter(!isNotInWorkspace(_)).map(x => {
      val absolutePath = System.getProperty("user.dir") + File.separator + x
      val simplePath = PathHelper.getSimplePathOfFile(absolutePath)
      val content = SgitReaderImpl.getContentOfFile(absolutePath)
      Blob.newBlobWithContent(content, simplePath)
    })

    //we add all files that need to
    recAdd(newFilesBlobs ++ blobsToRemove, indexBlobs)

  }

  @tailrec
  def recAdd(filesBlobs: Array[EntryTree], index: Array[EntryTree]): Unit = {
    //if no more files to deal with, we save the index
    if (filesBlobs.length == 0) SgitWriterImpl.updateIndex(index)
    else {
      //blob to delete, we update the index
      if (filesBlobs(0).hash.isEmpty) {
        recAdd(filesBlobs.tail, index.filter(x => !x.path.equals(filesBlobs(0).path)))
      }
      //else blob to add or update
      else {
        //we create the blob in memory if it doesn't already exists
        SgitWriterImpl.createObject(filesBlobs(0).contentString.get)
        //we update the index:

        //new blob
        if (index.filter(_.path.equals(filesBlobs(0).path)).isEmpty)
          recAdd(filesBlobs.tail, index :+ filesBlobs(0))

        //blob updated
        else {
          index.map(x =>
            if (x.path.equals(filesBlobs(0).path)) x.hash = filesBlobs(0).hash)
          recAdd(filesBlobs.tail, index)
        }
      }
    }
  }

}