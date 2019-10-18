package fr.missoum

import fr.missoum.commands._
import fr.missoum.logic.EntryTree
import fr.missoum.utils.helpers.PathHelper
import fr.missoum.utils.io.workspace.{WorkspaceManager, WorkspaceManagerImpl}
import fr.missoum.utils.io.{inputs, printers, readers, writers}
import inputs._
import printers._
import readers._
import writers._

object CommandExecutorImpl extends CommandExecutor {

  // io managers
  var sgitReader: SgitReader = SgitReaderImpl
  var workspaceReader: WorkspaceManager = WorkspaceManagerImpl
  var sgitWriter: SgitWriter = SgitWriterImpl
  var printer: ConsolePrinter = ConsolePrinterImpl
  var inputManager: UserInput = UserInputImpl
  //commands helpers
  var commitHelper: SgitCommit = SgitCommitImpl
  var statusHelper: SgitStatus = SgitStatusImpl
  var addHelper: SgitAdd = SgitAddImpl
  var logHelper: SgitLog = SgitLogImpl
  var checkoutHelper: SgitCheckout = SgitCheckoutImpl
  var diffHelper: SgitDiff = SgitDiffImpl

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
    if (notExistingFiles.nonEmpty)
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
      sgitWriter.createNewBranch(newBranch, sgitReader.getLastCommitOfCurrentBranch)
      printer.branchCreated(newBranch)
    }
  }

  def executeCreateNewTag(newTag: String): Unit = {
    if (sgitReader.isExistingTag(newTag))
      printer.tagAlreadyExists(newTag)
    else {
      sgitWriter.createNewTag(newTag, sgitReader.getLastCommitOfCurrentBranch)
      printer.tagCreated(newTag)
    }
  }

  def executeCommit(): Unit = {

    val isFirstCommit = !sgitReader.isExistingCommitOnCurrentBranch
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
    val isFirstCommit = !sgitReader.isExistingCommitOnCurrentBranch
    val lastCommit = commitHelper.getBlobsLastCommit(isFirstCommit)

    //process
    val untrackedFiles = statusHelper.getUntrackedFiles(workspace, index)
    val notStaged = statusHelper.getChangesNotStagedForCommit(index, workspace)
    val toBeCommitted = statusHelper.getChangesToBeCommitted(index, lastCommit)
    var printed = false
    //print
    printer.branch(branch)

    if (toBeCommitted.nonEmpty) {
      printer.changesToBeCommitted(toBeCommitted.get._1.map(_.path), toBeCommitted.get._2.map(_.path), toBeCommitted.get._3.map(_.path))
      printed = true
    }
    if (notStaged.nonEmpty) {
      printed = true
      printer.changesNotStagedForCommit(notStaged.get._1.map(_.path), notStaged.get._2.map(_.path))
    }
    if (untrackedFiles.nonEmpty) {
      printed = true
      printer.untrackedFiles(untrackedFiles.get.map(_.path))
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


  def executeCheckout(switchTo: String): Unit = {
    // We retrieve the hash commit that corresponds to the switch
    var hashCommit: String = ""
    var head: String = switchTo
    val isCheckoutBranch = sgitReader.isExistingBranch(switchTo)
    if (isCheckoutBranch)
      hashCommit = sgitReader.getLastCommitOfBranch(switchTo)
    else {
      if (sgitReader.isExistingTag(switchTo)) {
        hashCommit = sgitReader.getCommitTag(switchTo)
        head = hashCommit
      }
      else if (sgitReader.isExistingCommit(switchTo)) {
        hashCommit = switchTo
        head = switchTo
      }
    }
    //if switch doesn't exist
    if (hashCommit.isEmpty)
      printer.notExistingSwitch(switchTo)
    //else switch exists
    else {
      val index = sgitReader.getIndex
      val checkoutBlobs = commitHelper.getBlobsOfCommit(sgitReader.getCommit(hashCommit))

      //if it exists files modified since the switch commit but not committed => switch impossible
      val isFirstCommit = !sgitReader.isExistingCommitOnCurrentBranch
      val lastCommit = commitHelper.getBlobsLastCommit(isFirstCommit)
      val checkoutNotAllowedOn = checkoutHelper.checkoutNotAllowedOn(lastCommit, index, checkoutBlobs)
      if (checkoutNotAllowedOn.nonEmpty)
        printer.notAllowedCheckout(checkoutNotAllowedOn)
      else {
        switch(head, index, checkoutBlobs)
      }
    }
  }

  private def switch(head: String, currentIndex: List[EntryTree], checkoutBlobs: List[EntryTree]): Unit = {
    sgitWriter.setHeadBranch(head)
    sgitWriter.updateIndex(checkoutBlobs)
    val toDelete = checkoutHelper.findFilesToDelete(currentIndex, checkoutBlobs)
    val toCreate = checkoutHelper.findFilesToCreate(currentIndex, checkoutBlobs)
    workspaceReader.updateWorkspace(toDelete, toCreate)
  }

  def executeDiff(): Unit = {
    val workspace = workspaceReader.getAllBlobsOfWorkspace()
    val index = sgitReader.getIndex
    val notStaged = statusHelper.getChangesNotStagedForCommit(index, workspace)
    if (notStaged.nonEmpty) {
      val (modifiedNotStaged, deletedNotStaged) = notStaged.get

      modifiedNotStaged.map(x => {
        val absPath = PathHelper.getAbsolutePathOfFile(x.path)
        val oldContent = sgitReader.getContentOfObjectInString(x.hash).split("\n").toList
        val newContent = workspaceReader.getContentOfFile(absPath)
        printer.printSingleDiff(x.path, diffHelper.diff(oldContent, newContent))
      })

      deletedNotStaged.map(x => {
        val oldContent = sgitReader.getContentOfObjectInString(x.hash).split("\n").toList
        printer.printSingleDiff(x.path, diffHelper.diff(oldContent, List[String]()))
      })
    }
  }


}

