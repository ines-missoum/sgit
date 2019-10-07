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

  def executeAdd(files: Array[String]) = {
    //just for array(0)

    val path = System.getProperty("user.dir") + "/" + files(0)
    val content = SgitReader.getContentOfFile(path)

    SgitWriter.createBlob(content)
    SgitWriter.addToIndex(HashHelper.hashFile(content), path)

    //TODO for all possible arguments => récursif ?
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