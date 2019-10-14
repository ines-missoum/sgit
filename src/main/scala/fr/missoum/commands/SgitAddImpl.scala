package fr.missoum.commands

import java.io.File

import fr.missoum.logic.{Blob, EntryTree}
import fr.missoum.utils.helpers.PathHelper
import fr.missoum.utils.io.readers._
import fr.missoum.utils.io.writers._

import scala.annotation.tailrec

object SgitAddImpl extends SgitAdd {

  var workspaceReader: WorkspaceReader = WorkspaceReaderImpl
  var sgitReader: SgitReader = SgitReaderImpl
  var sgitWriter: SgitWriter = SgitWriterImpl

  /**
   * Checks if the file name exists
   *
   * @param fileName : Name of the file
   * @return True if the file do not exists either in workspace nor in index, otherwise false
   */
  def isNotExistingFile(fileName: String): Boolean = {
    val indexBlobsAbsolutePaths = sgitReader.getIndex.map(x => PathHelper.getAbsolutePathOfFile(x.path))
    val fileAbsolutePath = System.getProperty("user.dir") + File.separator + fileName
    val isExistInWorkspace = workspaceReader.fileExists(fileAbsolutePath)
    val isExistingInIndex = indexBlobsAbsolutePaths.exists(_.equals(fileAbsolutePath))
    !isExistInWorkspace && !isExistingInIndex
  }

  /**
   * Builds the blobs to update the index and recursively makes the changes in the index
   *
   * @param filesNames : List of files names that exist in the index
   */
  def addAll(filesNames: Array[String]): Unit = {

    val filesAbsolutePath = filesNames
      .map(System.getProperty("user.dir") + File.separator + _)

    //files to delete <=> files that don't exist but that are contain in the index
    val blobsToRemove = filesAbsolutePath
      .filter(!workspaceReader.fileExists(_))
      .map(x => Blob("", PathHelper.getSimplePathOfFile(x)))
    //files to add or update
    val newFilesBlobs: Array[EntryTree] = filesAbsolutePath
      .filter(workspaceReader.fileExists(_))
      .map(x => Blob.newBlobWithContent(sgitReader.getContentOfFile(x), PathHelper.getSimplePathOfFile(x)))

    //we add all files that need to
    val indexUpdated = recAdd(newFilesBlobs ++ blobsToRemove, sgitReader.getIndex)
    sgitWriter.updateIndex(indexUpdated)

  }

  /**
   * From the files blobs update the index
   * @param filesBlobs blobs modified that need to be save in the index
   * @param index index to update
   * @return the index updated
   */
  @tailrec
  def recAdd(filesBlobs: Array[EntryTree], index: Array[EntryTree]): Array[EntryTree] = {
    //if no more files to deal with, we save the index
    if (filesBlobs.length == 0) index
    //else we update the index and deal with the next blob
    else {
      //if blob to delete
      if (filesBlobs(0).hash.isEmpty)
        recAdd(filesBlobs.tail, index.filter(!_.path.equals(filesBlobs(0).path)))
      //else blob to add or to update
      else {
        //we create the blob in memory if it doesn't already exists
        sgitWriter.createObject(filesBlobs(0).contentString.get)

        //new blob
        if (!index.exists(_.path.equals(filesBlobs(0).path)))
          recAdd(filesBlobs.tail, index :+ filesBlobs(0))
        //blob updated
        else {
          val indexUpdated = index
          indexUpdated.map(x =>
            if (x.path.equals(filesBlobs(0).path)) x.hash = filesBlobs(0).hash)
          recAdd(filesBlobs.tail, indexUpdated)
        }
      }
    }
  }

}