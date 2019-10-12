package fr.missoum.utils.io.printers

import fr.missoum.logic.EntryTree

trait ConsolePrinter {
  def nothingToCommit(branch: String)

  def commitCreatedMessage(branch: String, message: String, nbFilesChanged: Int)

  def fileNotExist(fileName: String)

  def notExistingSgitRepository()

  def noCommand()

  def notValidCommand(wrongCommand: String)

  def notValidArguments(command: String, possibleInput: String)

  def sgitFolderAlreadyExists()

  def sgitFolderCreated()

  def branchCreated(newBranch: String)

  def branchAlreadyExists(existingBranch: String)

  def tagCreated(newTag: String)

  def tagAlreadyExists(existingTag: String)

  def printBranchesAndTags(currentBranch: String, tags: Array[String], branches: Array[String])

  def askEnterMessageCommits()

  def untrackedFiles(untrackedFiles: Array[String])

}