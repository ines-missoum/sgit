package fr.missoum

;

import fr.missoum.utils.{ConsolePrinter, SgitReader, SgitWriter}

object CommandExecutor {

  def executeInit(): Unit = {
    if (SgitReader.isExistingSgitFolder())
      ConsolePrinter.sgitFolderAlreadyExists()
    else {
      SgitWriter.createSgitRepository()
      ConsolePrinter.sgitFolderCreated()
    }
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
}