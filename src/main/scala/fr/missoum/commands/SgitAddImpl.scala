package fr.missoum.commands

import java.io.File

import fr.missoum.logic.EntryTree
import fr.missoum.utils.helpers.PathHelper

import scala.annotation.tailrec

object SgitAddImpl extends SgitAdd {

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
   * @param index         : Blobs of the index
   * @param blobsToRemove the blobs to delete of the index
   * @param newFilesBlobs the blobs to update or create in the index
   * @return the index updated and the list of blobs to create in memory
   */
  def addAll(index: List[EntryTree], blobsToRemove: List[EntryTree], newFilesBlobs: List[EntryTree]): (List[EntryTree], List[EntryTree]) = {
    //we add all files that need to
    recAdd(newFilesBlobs ++ blobsToRemove, index, List[EntryTree]())
  }

  /**
   * Retrieve all the blobs that should be deleted of the index and updated or created
   *
   * @param filesNames : List of files names that exist in the index
   * @param workspace  : Blobs of the workspace
   * @param local      : Where the command is made
   * @return two list : one of the blobs path that should be deleted and one of the blobs path that should be updated or created
   */
  def findUpdatesIndex(filesNames: List[String], workspace: List[EntryTree], local: String): (List[String], List[String]) = {

    val filesRelativePath = filesNames
      .map(x => PathHelper.getSimplePathOfFile(local + File.separator + x))
    //files to delete <=> files that don't exist but that are contain in the index
    val blobsToRemove = filesRelativePath
      .filter(x => !workspace.exists(y => x.equals(y.path)))
    val newFilesBlobs = filesRelativePath
      .filter(x => workspace.exists(y => x.equals(y.path)))
    (blobsToRemove, newFilesBlobs)
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