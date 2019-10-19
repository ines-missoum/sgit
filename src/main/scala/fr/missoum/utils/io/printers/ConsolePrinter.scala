package fr.missoum.utils.io.printers

import fr.missoum.logic.Commit

trait ConsolePrinter {
  def noCreationPossible(): Unit

  def printSingleDiff(path: String, value: List[String]): Unit

  def notAllowedCheckout(modifiedFiles: List[String]): Unit

  def changesNotStagedForCommit(modifiedNotStaged: List[String], deletedNotStaged: List[String]): Unit

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

  def printBranchesAndTags(currentBranch: String, tags: List[String], branches: List[String])

  def askEnterMessageCommits()

  def untrackedFiles(untrackedFiles: List[String])

  def changesToBeCommitted(news: List[String], modified: List[String], deleted: List[String])

  def branch(branch: String)

  def displayAllCommits(commits: List[Commit], branch: String): Unit

  def noLog(branch: String): Unit

  def statusAllGood(): Unit

  def notExistingSwitch(switchTo: String): Unit

}