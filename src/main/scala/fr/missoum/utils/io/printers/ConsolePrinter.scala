package fr.missoum.utils.io.printers

trait ConsolePrinter{
  def NothingToCommit(branch: String)

  def CommitCreatedMessage(branch: String,message:String,nbFilesChanged:Int)

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

}