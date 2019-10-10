package fr.missoum

import fr.missoum.commands.SgitAdd
import fr.missoum.utils.io.printers.{ConsolePrinter, ConsolePrinterImpl}
import fr.missoum.utils.io.readers.{SgitReader, SgitReaderImpl}
import fr.missoum.utils.io.writers.{SgitWriter, SgitWriterImpl}

object CommandExecutorImpl extends CommandExecutor {

  var sgitReader: SgitReader = SgitReaderImpl
  var sgitWriter: SgitWriter = SgitWriterImpl
  var printer: ConsolePrinter = ConsolePrinterImpl

  def isCommandForbiddenHere(): Boolean = !SgitReaderImpl.isExistingSgitFolder


  def executeInit(): Unit = {
    if (sgitReader.isExistingSgitFolder())
      printer.sgitFolderAlreadyExists()
    else {
      sgitWriter.createSgitRepository()
      printer.sgitFolderCreated()
    }
  }

  def executeAdd(filesNames: Array[String], linesToAddInIndex: String): Unit = {
    val notExistingFiles = SgitAdd.getNotExistingFile(filesNames)
    //if there's not existing file(s), we inform the user and don't add any files
    if(!notExistingFiles.isEmpty)
      notExistingFiles.map(printer.fileNotExist(_))
    //else we add all the existing files
    else
      SgitAdd.addAll(filesNames)
  }


  def executeGetAllBranchesAndTags() = {
    val currentBranch = sgitReader.getCurrentBranch()
    val tags = sgitReader.getAllTags()
    val branches = sgitReader.getAllBranches()
    printer.printBranchesAndTags(currentBranch, tags, branches)
  }


  def executeCreateNewBranch(newBranch: String) = {
    if (sgitReader.isExistingBranch(newBranch))
      printer.branchAlreadyExists(newBranch)
    else {
      sgitWriter.createNewBranch(newBranch)
      printer.branchCreated(newBranch)
    }
  }

  def executeCreateNewTag(newTag: String) = {
    if (sgitReader.isExistingTag(newTag))
      printer.tagAlreadyExists(newTag)
    else {
      SgitWriterImpl.createNewTag(newTag)
      printer.tagCreated(newTag)
    }
  }


}