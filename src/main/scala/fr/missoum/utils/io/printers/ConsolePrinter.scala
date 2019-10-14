package fr.missoum.utils.io.printers

import fr.missoum.logic.Commit

trait ConsolePrinter {

  def changesNotStagedForCommit(modifiedNotStaged: Array[String], deletedNotStaged: Array[String]): Unit

  def nothingToCommit(branch: String): Unit

  def commitCreatedMessage(branch: String, message: String, nbFilesChanged: Int): Unit

  def fileNotExist(fileName: String): Unit

  def notExistingSgitRepository(): Unit

  def noCommand(): Unit

  def notValidCommand(wrongCommand: String): Unit

  def notValidArguments(command: String, possibleInput: String): Unit

  def sgitFolderAlreadyExists(): Unit

  def sgitFolderCreated(): Unit

  def branchCreated(newBranch: String): Unit

  def branchAlreadyExists(existingBranch: String): Unit

  def tagCreated(newTag: String)

  def tagAlreadyExists(existingTag: String)

  def printBranchesAndTags(currentBranch: String, tags: Array[String], branches: Array[String])

  def askEnterMessageCommits()

  def untrackedFiles(untrackedFiles: Array[String])

  def changesToBeCommitted(news: Array[String], modified: Array[String], deleted: Array[String])

  def branch(branch: String)

  def displayAllCommits(commits: Array[Commit], branch: String): Unit

}