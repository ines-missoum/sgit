package fr.missoum

import fr.missoum.commands._
import fr.missoum.utils.io.{inputs, printers, readers, writers}, inputs._, printers._, readers._, writers._

object CommandExecutorImpl extends CommandExecutor {

  // io managers
  var sgitReader: SgitReader = SgitReaderImpl
  var workspaceReader: WorkspaceReader = WorkspaceReaderImpl
  var sgitWriter: SgitWriter = SgitWriterImpl
  var printer: ConsolePrinter = ConsolePrinterImpl
  var inputManager: UserInput = UserInputImpl
  //commands helpers
  var commitHelper: SgitCommit = SgitCommitImpl
  var statusHelper: SgitStatus = SgitStatusImpl
  var addHelper: SgitAdd = SgitAddImpl

  def isCommandForbiddenHere(): Boolean = !sgitReader.isExistingSgitFolder


  def executeInit(): Unit = {
    if (sgitReader.isExistingSgitFolder())
      printer.sgitFolderAlreadyExists()
    else {
      sgitWriter.createSgitRepository()
      printer.sgitFolderCreated()
    }
  }

  def executeAdd(filesNames: Array[String], linesToAddInIndex: String): Unit = {
    val notExistingFiles = filesNames.filter(addHelper.isNotExistingFile(_))
    //if there's not existing file(s), we inform the user and don't add any files
    if (!notExistingFiles.isEmpty)
      printer.fileNotExist(notExistingFiles(0))
    //else we add all the existing files
    else
      addHelper.addAll(filesNames)
  }


  def executeGetAllBranchesAndTags(): Unit = {
    val currentBranch = sgitReader.getCurrentBranch()
    val tags = sgitReader.getAllTags()
    val branches = sgitReader.getAllBranches()
    printer.printBranchesAndTags(currentBranch, tags, branches)
  }


  def executeCreateNewBranch(newBranch: String): Unit = {
    if (sgitReader.isExistingBranch(newBranch))
      printer.branchAlreadyExists(newBranch)
    else {
      sgitWriter.createNewBranch(newBranch)
      printer.branchCreated(newBranch)
    }
  }

  def executeCreateNewTag(newTag: String): Unit = {
    if (sgitReader.isExistingTag(newTag))
      printer.tagAlreadyExists(newTag)
    else {
      sgitWriter.createNewTag(newTag)
      printer.tagCreated(newTag)
    }
  }

  def executeCommit(): Unit = {

    val isFirstCommit = !sgitReader.isExistingCommit()
    val blobsToCommit = commitHelper.getBlobsToCommit(isFirstCommit)
    val branch = sgitReader.getCurrentBranch()

    //if nothing to commit
    if (blobsToCommit.isEmpty)
      printer.nothingToCommit(branch)
    else {
      //if commit needed
      printer.askEnterMessageCommits()
      val message = inputManager.retrieveUserCommitMessage()
      val (commit, nbFilesChanged) = commitHelper.commit(isFirstCommit, branch, blobsToCommit, message)
      sgitWriter.saveCommit(commit, branch)
      printer.commitCreatedMessage(branch, message, nbFilesChanged)
    }
  }

  def executeStatus(): Unit = {
    //retrieve data
    val branch = sgitReader.getCurrentBranch()
    val workspace = workspaceReader.getAllBlobsOfWorkspace()
    val index = sgitReader.getIndex()
    val isFirstCommit = !sgitReader.isExistingCommit()
    val lastCommit = commitHelper.getAllBlobsCommitted(isFirstCommit)

    //process
    val untrackedFiles = statusHelper.getUntrackedFiles(workspace, index)
    val (modifiedNotStaged, deletedNotStaged) = statusHelper.getChangesNotStagedForCommit(index, workspace)
    val (newsToCommit, modifiedToCommit, deletedToCommit) = statusHelper.getChangesToBeCommitted(index, lastCommit)

    //print
    printer.branch(branch)
    if (!(newsToCommit.isEmpty && modifiedToCommit.isEmpty && deletedToCommit.isEmpty))
      printer.changesToBeCommitted(newsToCommit, modifiedToCommit, deletedToCommit)
    if (!(modifiedNotStaged.isEmpty && deletedNotStaged.isEmpty))
      printer.changesNotStagedForCommit(modifiedNotStaged, deletedNotStaged)
    if (!untrackedFiles.isEmpty)
      printer.untrackedFiles(untrackedFiles)
    //when only not staged => no changes added to commit (use "git add" and/or "git commit -a")
  }


}