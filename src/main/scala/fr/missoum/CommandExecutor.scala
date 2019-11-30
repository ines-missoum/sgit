package fr.missoum

import fr.missoum.commands.{SgitCommit, SgitDiff, SgitLog, SgitStatus}
import fr.missoum.utils.io.inputs.UserInput
import fr.missoum.utils.io.printers.ConsolePrinter
import fr.missoum.utils.io.readers.SgitReader
import fr.missoum.utils.io.workspace.WorkspaceManager
import fr.missoum.utils.io.writers.SgitWriter

trait CommandExecutor {

  var sgitReader: SgitReader
  var workspaceReader: WorkspaceManager
  var sgitWriter: SgitWriter
  var printer: ConsolePrinter
  var commitHelper: SgitCommit
  var statusHelper: SgitStatus
  var inputManager: UserInput
  var logHelper: SgitLog
  var diffHelper: SgitDiff

  def isCommandForbiddenHere(): Boolean

  def executeInit(): Unit

  def executeAdd(filesNames: List[String]): Unit

  def executeGetAllBranchesAndTags()

  def executeCreateNewBranch(newBranch: String)

  def executeCreateNewTag(newTag: String)

  def executeCommit(message: String)

  def executeStatus()

  def executeLog(): Unit

  def executeLogP(): Unit

  def executeCheckout(switchTo: String): Unit

  def executeDiff(): Unit

}