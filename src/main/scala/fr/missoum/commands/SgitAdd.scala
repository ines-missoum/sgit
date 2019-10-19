package fr.missoum.commands

import fr.missoum.logic.EntryTree
import fr.missoum.utils.io.readers.SgitReader
import fr.missoum.utils.io.workspace.WorkspaceManager
import fr.missoum.utils.io.writers.SgitWriter

trait SgitAdd {

  var workspaceReader: WorkspaceManager
  var sgitReader: SgitReader
  var sgitWriter: SgitWriter

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
   *
   * @param filesNames : List of files names that exist in the index
   */
  def addAll(filesNames: List[String]): Unit


}