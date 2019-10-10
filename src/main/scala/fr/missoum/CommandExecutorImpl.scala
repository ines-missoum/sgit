package fr.missoum

import fr.missoum.commands.SgitAdd
import fr.missoum.utils.io.printers.ConsolePrinterImpl
import fr.missoum.utils.io.readers.SgitReaderImpl
import fr.missoum.utils.io.writers.SgitWriterImpl

object CommandExecutorImpl extends CommandExecutor {

  def isCommandForbiddenHere(): Boolean = !SgitReaderImpl.isExistingSgitFolder


  def executeInit(): Unit = {
    if (SgitReaderImpl.isExistingSgitFolder())
      ConsolePrinterImpl.sgitFolderAlreadyExists()
    else {
      SgitWriterImpl.createSgitRepository()
      ConsolePrinterImpl.sgitFolderCreated()
    }
  }

  def executeAdd(filesNames: Array[String], linesToAddInIndex: String): Unit = {
    val notExistingFiles = SgitAdd.getNotExistingFile(filesNames)
    //if there's not existing file(s), we inform the user and don't add any files
    if(!notExistingFiles.isEmpty)
      notExistingFiles.map(ConsolePrinterImpl.fileNotExist(_))
    //else we add all the existing files
    else
      SgitAdd.addAll(filesNames)
  }


  def executeGetAllBranchesAndTags() = {
    val currentBranch = SgitReaderImpl.getCurrentBranch()
    val tags = SgitReaderImpl.getAllTags()
    val branches = SgitReaderImpl.getAllBranches()
    ConsolePrinterImpl.printBranchesAndTags(currentBranch, tags, branches)
  }


  def executeCreateNewBranch(newBranch: String) = {
    if (SgitReaderImpl.isExistingBranch(newBranch))
      ConsolePrinterImpl.branchAlreadyExists(newBranch)
    else {
      SgitWriterImpl.createNewBranch(newBranch)
      ConsolePrinterImpl.branchCreated(newBranch)
    }
  }

  def executeCreateNewTag(newTag: String) = {
    if (SgitReaderImpl.isExistingTag(newTag))
      ConsolePrinterImpl.tagAlreadyExists(newTag)
    else {
      SgitWriterImpl.createNewTag(newTag)
      ConsolePrinterImpl.tagCreated(newTag)
    }
  }
}