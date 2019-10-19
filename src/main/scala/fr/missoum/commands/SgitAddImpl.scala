package fr.missoum.commands

import java.io.File

import fr.missoum.logic.{Blob, EntryTree}
import fr.missoum.utils.helpers.PathHelper
import fr.missoum.utils.io.readers._
import fr.missoum.utils.io.workspace.{WorkspaceManager, WorkspaceManagerImpl}
import fr.missoum.utils.io.writers._

import scala.annotation.tailrec

object SgitAddImpl extends SgitAdd {

  var workspaceReader: WorkspaceManager = WorkspaceManagerImpl
  var sgitReader: SgitReader = SgitReaderImpl
  var sgitWriter: SgitWriter = SgitWriterImpl

  /**
   * Checks if the file name exists
   *
   * @param fileName  : Name of the file
   * @param index     : Blobs of the index
   * @param workspace : Blobs of the workspace
   * @param local     : Where the command is made
   * @return True if the file do not exists either in workspace nor in index, otherwise false
   */
  def isNotExistingFile(fileName: String, index: List[EntryTree], workspace: List[EntryTree], local: String): Boolean = {
    val fileRelativePath = PathHelper.getSimplePathOfFile(local + File.separator + fileName)
    val isExistInWorkspace = workspace.exists(_.path.equals(fileRelativePath))
    val isExistingInIndex = index.exists(_.path.equals(fileRelativePath))
    !isExistInWorkspace && !isExistingInIndex
  }

  /**
   * Builds the blobs to update the index and recursively makes the changes in the index
   *
   * @param filesNames : List of files names that exist in the index
   */
  def addAll(filesNames: List[String]): Unit = {

    val filesAbsolutePath = filesNames
      .map(System.getProperty("user.dir") + File.separator + _)

    //files to delete <=> files that don't exist but that are contain in the index
    val blobsToRemove = filesAbsolutePath
      .filter(!workspaceReader.fileExists(_))
      .map(x => Blob("", PathHelper.getSimplePathOfFile(x)))
    //files to add or update
    val newFilesBlobs: List[EntryTree] = filesAbsolutePath
      .filter(workspaceReader.fileExists(_))
      .map(x => Blob.newBlobWithContent(sgitReader.getContentOfFile(x), PathHelper.getSimplePathOfFile(x)))

    //we add all files that need to
    val (indexUpdated, blobsToCreate) = recAdd(newFilesBlobs ++ blobsToRemove, sgitReader.getIndex, List[EntryTree]())
    //we create the blob in memory if it doesn't already exists
    blobsToCreate.map(x => sgitWriter.createObject(x.contentString.get))
    sgitWriter.updateIndex(indexUpdated)

  }

  /**
   * From the files blobs update the index
   *
   * @param filesBlobs    blobs modified that need to be save in the index (empty path for the ones that need to be deleted)
   * @param index         index to update
   * @param blobsToCreate list of blobs to create in memory
   * @return the index updated and the list of blobs to create in memory
   */
  @tailrec
  def recAdd(filesBlobs: List[EntryTree], index: List[EntryTree], blobsToCreate: List[EntryTree]): (List[EntryTree], List[EntryTree]) = {
    //if no more files to deal with, we save the index
    if (filesBlobs.isEmpty) (index, blobsToCreate)
    //else we update the index and deal with the next blob
    else {
      //if blob to delete
      if (filesBlobs(0).hash.isEmpty)
        recAdd(filesBlobs.tail, index.filter(!_.path.equals(filesBlobs(0).path)), blobsToCreate)
      //else blob to add or to update
      else {

        //new blob
        if (!index.exists(_.path.equals(filesBlobs(0).path)))
          recAdd(filesBlobs.tail, index :+ filesBlobs(0), blobsToCreate :+ filesBlobs(0))
        //blob updated
        else {
          val indexUpdated = index
          indexUpdated.map(x =>
            if (x.path.equals(filesBlobs(0).path)) x.hash = filesBlobs(0).hash)
          recAdd(filesBlobs.tail, indexUpdated, blobsToCreate :+ filesBlobs(0))
        }
      }
    }
  }

}