package fr.missoum

trait CommandExecutor {

  def isCommandForbiddenHere(): Boolean

  def executeInit(): Unit

  def executeAdd(filesNames: Array[String], linesToAddInIndex: String): Unit

  def executeGetAllBranchesAndTags()

  def executeCreateNewBranch(newBranch: String)

  def executeCreateNewTag(newTag: String)

}