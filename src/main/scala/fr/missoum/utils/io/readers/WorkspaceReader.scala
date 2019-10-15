package fr.missoum.utils.io.readers

import fr.missoum.logic.EntryTree

/**
 * This trait gives the role of accessor of files or directories of the workspace.
 */
trait WorkspaceReader {

  def fileExists(path: String): Boolean

  def getAllBlobsOfWorkspace(): List[EntryTree]
}