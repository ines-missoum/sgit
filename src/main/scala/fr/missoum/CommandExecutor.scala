package fr.missoum

import fr.missoum.commands.SgitAdd
import fr.missoum.utils.io.{ConsolePrinter, SgitReader, SgitWriter}

object CommandExecutor {

  def isCommandForbiddenHere(): Boolean = !SgitReader.isExistingSgitFolder


  def executeInit(): Unit = {
    if (SgitReader.isExistingSgitFolder())
      ConsolePrinter.sgitFolderAlreadyExists()
    else {
      SgitWriter.createSgitRepository()
      ConsolePrinter.sgitFolderCreated()
    }
  }

  def executeAdd(filesNames: Array[String], linesToAddInIndex: String): Unit = {
    val notExistingFiles = SgitAdd.getNotExistingFile(filesNames)
    //if there's not existing file(s), we inform the user and don't add any files
    if(!notExistingFiles.isEmpty)
      notExistingFiles.map(ConsolePrinter.fileNotExist(_))
    //else we add all the existing files
    else
      SgitAdd.addAll(filesNames)
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