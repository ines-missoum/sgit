package fr.missoum.commands

import fr.missoum.logic.EntryTree

trait SgitAdd {

  /**
   * Retrieve all the blobs that should be deleted of the index and updated or created
   *
   * @param filesNames : List of files names that exist in the index
   * @param workspace  : Blobs of the workspace
   * @param local      : Where the command is made
   * @return two list : one of the blobs path that should be deleted and one of the blobs path that should be updated or created
   */
  def findUpdatesIndex(filesNames: List[String], workspace: List[EntryTree], local: String): (List[String], List[String])

  /**
   * Checks if the file name exists
   *
   * @param fileName  : Name of the file
   * @param index     : Blobs of the index
   * @param workspace : Blobs of the workspace
   * @param local     : Where the command is made
   * @return True if the file do not exists either in workspace nor in index, otherwise false
   */
  def isNotExistingFile(fileName: String, index: List[EntryTree], workspace: List[EntryTree], local: String): Boolean

  /**
   * Builds the blobs to update the index and recursively makes the changes in the index
   * @param index : Blobs of the index
   * @param blobsToRemove the blobs to delete of the index
   * @param newFilesBlobs the blobs to update or create in the index
   * @return the index updated and the list of blobs to create in memory
   */
  def addAll(index: List[EntryTree], blobsToRemove: List[EntryTree], newFilesBlobs: List[EntryTree]): (List[EntryTree], List[EntryTree])

}