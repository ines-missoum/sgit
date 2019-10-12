package fr.missoum

import fr.missoum.commands.{SgitAdd, SgitCommit, SgitCommitImpl, SgitStatus, SgitStatusImpl}
import fr.missoum.logic.Blob
import fr.missoum.utils.io.inputs.{UserInput, UserInputImpl}
import fr.missoum.utils.io.printers.{ConsolePrinter, ConsolePrinterImpl}
import fr.missoum.utils.io.readers.{SgitReader, SgitReaderImpl, WorkspaceReader, WorkspaceReaderImpl}
import fr.missoum.utils.io.writers.{SgitWriter, SgitWriterImpl}

object CommandExecutorImpl extends CommandExecutor {

  var sgitReader: SgitReader = SgitReaderImpl
  var workspaceReader: WorkspaceReader = WorkspaceReaderImpl
  var sgitWriter: SgitWriter = SgitWriterImpl
  var printer: ConsolePrinter = ConsolePrinterImpl
  var commitHelper: SgitCommit = SgitCommitImpl
  var inputManager: UserInput = UserInputImpl
  var statusHelper: SgitStatus = SgitStatusImpl

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
    if (!notExistingFiles.isEmpty)
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

  def executeCommit() = {

    val blobsToCommit = commitHelper.getBlobsToCommit()
    val branch = sgitReader.getCurrentBranch()
    //if nothing to commmit
    if (blobsToCommit.isEmpty)
      printer.nothingToCommit(branch)
    else {
      //if commit needed
      printer.askEnterMessageCommits
      val message = inputManager.retrieveUserCommitMessage()
      val nbFilesChanged = commitHelper.commit(blobsToCommit, message)
      printer.commitCreatedMessage(branch, message, nbFilesChanged)
    }
  }

  def executeStatus() = {
    val workspace = workspaceReader.getAllBlobsOfWorkspace()
    val index = sgitReader.getIndex().map(x => Blob(x))
    val lastCommit = commitHelper.retrievePreviousBlobsCommitted()

    val untrackedFiles = statusHelper.getUntrackedFiles(workspace, index)
    val (modifiedNotStaged, deletedNotStaged) = statusHelper.getChangesNotStagedForCommit(index, workspace)

    if (!(modifiedNotStaged.isEmpty && deletedNotStaged.isEmpty))
      printer.changesNotStagedForCommit(modifiedNotStaged, deletedNotStaged)
    if (!untrackedFiles.isEmpty)
      printer.untrackedFiles(untrackedFiles)

  }


}