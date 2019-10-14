package fr.missoum

import fr.missoum.commands.{SgitCommit, SgitLog, SgitStatus}
import fr.missoum.utils.io.inputs.UserInput
import fr.missoum.utils.io.printers.ConsolePrinter
import fr.missoum.utils.io.readers.{SgitReader, WorkspaceReader}
import fr.missoum.utils.io.writers.SgitWriter

trait CommandExecutor {

  var sgitReader: SgitReader
  var workspaceReader : WorkspaceReader
  var sgitWriter: SgitWriter
  var printer: ConsolePrinter
  var commitHelper: SgitCommit
  var statusHelper: SgitStatus
  var inputManager : UserInput
  var logHelper: SgitLog

  def isCommandForbiddenHere(): Boolean

  def executeInit(): Unit

  def executeAdd(filesNames: Array[String], linesToAddInIndex: String): Unit

  def executeGetAllBranchesAndTags()

  def executeCreateNewBranch(newBranch: String)

  def executeCreateNewTag(newTag: String)

  def executeCommit()

  def executeStatus()

  def executeLog(): Unit

}