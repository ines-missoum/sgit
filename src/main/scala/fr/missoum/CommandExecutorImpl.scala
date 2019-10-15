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
  var logHelper: SgitLog = SgitLogImpl

  def isCommandForbiddenHere(): Boolean = !sgitReader.isExistingSgitFolder


  def executeInit(): Unit = {
    if (sgitReader.isExistingSgitFolder)
      printer.sgitFolderAlreadyExists()
    else {
      sgitWriter.createSgitRepository()
      printer.sgitFolderCreated()
    }
  }

  def executeAdd(filesNames: List[String]): Unit = {
    val notExistingFiles = filesNames.filter(addHelper.isNotExistingFile(_))
    //if there's not existing file(s), we inform the user and don't add any files
    if (!notExistingFiles.isEmpty)
      printer.fileNotExist(notExistingFiles(0))
    //else we add all the existing files
    else
      addHelper.addAll(filesNames)
  }


  def executeGetAllBranchesAndTags(): Unit = {
    val currentBranch = sgitReader.getCurrentBranch
    val tags = sgitReader.getAllTags
    val branches = sgitReader.getAllBranches
    printer.printBranchesAndTags(currentBranch, tags, branches)
  }


  def executeCreateNewBranch(newBranch: String): Unit = {
    if (sgitReader.isExistingBranch(newBranch))
      printer.branchAlreadyExists(newBranch)
    else {
      sgitWriter.createNewBranch(newBranch,sgitReader.getLastCommit)
      printer.branchCreated(newBranch)
    }
  }

  def executeCreateNewTag(newTag: String): Unit = {
    if (sgitReader.isExistingTag(newTag))
      printer.tagAlreadyExists(newTag)
    else {
      sgitWriter.createNewTag(newTag,sgitReader.getLastCommit)
      printer.tagCreated(newTag)
    }
  }

  def executeCommit(): Unit = {

    val isFirstCommit = !sgitReader.isExistingCommit
    val index = sgitReader.getIndex
    val blobsLastCommit = commitHelper.getBlobsLastCommit(isFirstCommit)
    val nbFilesChanged = commitHelper.nbFilesChangedSinceLastCommit(index, blobsLastCommit)
    val branch = sgitReader.getCurrentBranch

    //if nothing to commit
    if (nbFilesChanged.isEmpty)
      printer.nothingToCommit(branch)
    else {
      //if commit needed
      printer.askEnterMessageCommits()
      val message = inputManager.retrieveUserCommitMessage()
      val commit = commitHelper.commit(isFirstCommit, branch, index, message)
      sgitWriter.saveCommit(commit, branch)
      printer.commitCreatedMessage(branch, message, nbFilesChanged.get)
    }
  }

  def executeStatus(): Unit = {
    //retrieve data
    val branch = sgitReader.getCurrentBranch
    val workspace = workspaceReader.getAllBlobsOfWorkspace()
    val index = sgitReader.getIndex
    val isFirstCommit = !sgitReader.isExistingCommit
    val lastCommit = commitHelper.getBlobsLastCommit(isFirstCommit)

    //process
    val untrackedFiles = statusHelper.getUntrackedFiles(workspace, index)
    val notStaged = statusHelper.getChangesNotStagedForCommit(index, workspace)
    val toBeCommitted = statusHelper.getChangesToBeCommitted(index, lastCommit)
    var printed = false
    //print
    printer.branch(branch)

    if (!toBeCommitted.isEmpty) {
      printer.changesToBeCommitted(toBeCommitted.get._1, toBeCommitted.get._2, toBeCommitted.get._3)
      printed = true
    }
    if (!notStaged.isEmpty) {
      printed = true
      printer.changesNotStagedForCommit(notStaged.get._1, notStaged.get._2)
    }
    if (!untrackedFiles.isEmpty) {
      printed = true
      printer.untrackedFiles(untrackedFiles.get)
    }
    if (!printed)
      printer.statusAllGood()
  }

  def executeLog(): Unit = {

    val branch = sgitReader.getCurrentBranch
    val logs = logHelper.retrieveAllCommits()

    if (logs.isEmpty)
      printer.noLog(branch)
    else
      printer.displayAllCommits(logs, branch)
  }

}

