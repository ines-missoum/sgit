package fr.missoum.commands

import fr.missoum.utils.io.readers.{SgitReader, WorkspaceReader}
import fr.missoum.utils.io.writers.SgitWriter

trait SgitAdd {

  var workspaceReader: WorkspaceReader
  var sgitReader: SgitReader
  var sgitWriter: SgitWriter

  /**
   * Checks if the file name exists
   * @param fileName : Name of the file
   * @return True if the file do not exists either in workspace nor in index, otherwise false
   */
  def isNotExistingFile(fileName: String): Boolean

  /**
   * Builds the blobs to update the index and recursively makes the changes in the index
   * @param filesNames : List of files names that exist in the index
   */
  def addAll(filesNames: Array[String]): Unit


}