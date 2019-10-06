package fr.missoum;
import fr.missoum.utils.{ConsolePrinter, SgitReader, SgitWriter}

object CommandExecutor{

  def executeInit(): Unit ={
    if (SgitReader.isExistingSgitFolder())
      ConsolePrinter.sgitFolderAlreadyExists()
    else {
      SgitWriter.createSgitRepository()
      ConsolePrinter.sgitFolderCreated()
    }
  }

  def executeGetAllBranches()={

  }

  def executeGetAllBranchesAndInfo()={
    //TODO implement
  }

  def executeCreateNewBranch(newBranch:String)={
    if(SgitReader.isExistingBranch(newBranch))
      ConsolePrinter.branchAlreadyExists(newBranch)
    else {
      SgitWriter.createNewBranch(newBranch)
      ConsolePrinter.branchCreated(newBranch)
    }

  }
}