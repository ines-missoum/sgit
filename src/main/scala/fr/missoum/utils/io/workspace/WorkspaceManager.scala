package fr.missoum.utils.io.workspace

import fr.missoum.logic.EntryTree
import fr.missoum.utils.io.readers.SgitReader

/**
 * This trait gives the role of accessor of files or directories of the workspace.
 */
trait WorkspaceManager {

  var reader: SgitReader

  /**
   * From the lists of blobs that sould be deleted and created, it's updating the workspace content
   * @param toDelete blobs to delete from the workspace
   * @param toCreate blobs to create in the workspace
   */
  def updateWorkspace(toDelete: List[EntryTree], toCreate: List[EntryTree]): Unit


  def fileExists(path: String): Boolean

  def getAllBlobsOfWorkspace(): List[EntryTree]
}
