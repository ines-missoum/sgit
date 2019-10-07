package fr.missoum

;

import fr.missoum.utils.{ConsolePrinter, SgitReader, SgitWriter}

import scala.annotation.tailrec

object CommandExecutor {

  def isCommandForbiddenHere(): Boolean = (System.getProperty("user.dir")) != (SgitReader.getLocation())


  def executeInit(): Unit = {
    if (SgitReader.isExistingSgitFolder())
      ConsolePrinter.sgitFolderAlreadyExists()
    else {
      SgitWriter.createSgitRepository()
      ConsolePrinter.sgitFolderCreated()
    }
  }

  @tailrec
  def executeAdd(files: Array[String], linesToAddInIndex: String): Unit = {

    if (files.length == 0) SgitWriter.addToIndex(linesToAddInIndex)

    else {

      var indexLines = linesToAddInIndex
      val path = System.getProperty("user.dir") + "/" + files(0)
      val content = SgitReader.getContentOfFile(path)
      SgitWriter.createBlob(content)

      val line = SgitWriter.buildIndexLine(HashHelper.hashFile(content), path)

      //if not already staged, added to index
      if ((SgitReader.getIndex().find(line.contains).isEmpty)) {
        indexLines += line
        executeAdd(files.tail, (indexLines))
      }
      else
        executeAdd(files.tail, linesToAddInIndex)
    }
    //TODO for regexp and file doesn't exists
  }

  def executeGetAllBranchesAndTags() = {
    val currentBranch = SgitReader.getCurrentBranch()
    val tags = SgitReader.getAllTags()
    val branches = SgitReader.getAllBranches()
    ConsolePrinter.printBranchesAndTags(currentBranch, tags, branches)
  }


  def executeCreateNewBranch(newBranch: String) = {
    if (SgitReader.isExistingBranch(newBranch))
      ConsolePrinter.branchAlreadyExists(newBranch)
    else {
      SgitWriter.createNewBranch(newBranch)
      ConsolePrinter.branchCreated(newBranch)
    }
  }

  def executeCreateNewTag(newTag: String) = {
    if (SgitReader.isExistingTag(newTag))
      ConsolePrinter.tagAlreadyExists(newTag)
    else {
      SgitWriter.createNewTag(newTag)
      ConsolePrinter.tagCreated(newTag)
    }
  }
}