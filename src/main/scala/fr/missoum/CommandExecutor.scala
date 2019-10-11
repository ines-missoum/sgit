package fr.missoum

import fr.missoum.commands.SgitCommit
import fr.missoum.utils.io.printers.ConsolePrinter
import fr.missoum.utils.io.readers.SgitReader
import fr.missoum.utils.io.writers.SgitWriter

trait CommandExecutor {

  var sgitReader: SgitReader
  var sgitWriter: SgitWriter
  var printer: ConsolePrinter
  var commitHelper: SgitCommit

  def isCommandForbiddenHere(): Boolean

  def executeInit(): Unit

  def executeAdd(filesNames: Array[String], linesToAddInIndex: String): Unit

  def executeGetAllBranchesAndTags()

  def executeCreateNewBranch(newBranch: String)

  def executeCreateNewTag(newTag: String)

  def executeCommit()

}