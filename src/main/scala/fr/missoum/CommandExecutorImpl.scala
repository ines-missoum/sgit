package fr.missoum

import fr.missoum.commands._
import fr.missoum.logic.{Blob, EntryTree}
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
    val index = sgitReader.getIndex
    if (index.isEmpty)
      printer.improperSgitRepository()
    else {
      val workspace = workspaceReader.getAllBlobsOfWorkspace()
      val local = System.getProperty("user.dir")
      val notExistingFiles = filesNames.filter(addHelper.isNotExistingFile(_, index.get, workspace, local))
      //if there's not existing file(s), we inform the user and don't add any files
      if (notExistingFiles.nonEmpty)
        printer.fileNotExist(notExistingFiles(0))
      //else we add all the existing files
      else {
        val result = addHelper.findUpdatesIndex(filesNames, workspace, local)
        val blobsToRemove = result._1.map(x => Blob("", x))
        val newFilesBlobs = result._2.map(x => Blob.newBlobWithContent(workspaceReader.getContentOfFile(PathHelper.getAbsolutePathOfFile(x)).mkString("\n"), x))

        val (indexUpdated, blobsToCreate) = addHelper.addAll(index.get, blobsToRemove, newFilesBlobs)
        //we create the blob in memory if it doesn't already exists
        blobsToCreate.map(x => sgitWriter.createObject(x.contentString.get))
        sgitWriter.updateIndex(indexUpdated)
      }
    }
  }


  def executeGetAllBranchesAndTags(): Unit = {
    val currentBranch = sgitReader.getCurrentBranch.getOrElse("")
    val tags = sgitReader.getAllTags
    val branches = sgitReader.getAllBranches
    if (tags.nonEmpty && branches.nonEmpty)
      printer.printBranchesAndTags(currentBranch, tags.get, branches.get)
    else printer.improperSgitRepository()
  }


  def executeCreateNewBranch(newBranch: String): Unit = {
    val lastCommitHash = sgitReader.getLastCommitHash
    if (lastCommitHash.isEmpty)
      printer.noCreationPossible()
    else if (sgitReader.isExistingBranch(newBranch))
      printer.branchAlreadyExists(newBranch)
    else {
      sgitWriter.createNewBranch(newBranch, lastCommitHash.get)
      printer.branchCreated(newBranch)
    }
  }

  def executeCreateNewTag(newTag: String): Unit = {
    val lastCommitHash = sgitReader.getLastCommitHash
    if (lastCommitHash.isEmpty)
      printer.noCreationPossible()
    else if (sgitReader.isExistingTag(newTag))
      printer.tagAlreadyExists(newTag)
    else {
      sgitWriter.createNewTag(newTag, sgitReader.getLastCommitHash.get)
      printer.tagCreated(newTag)
    }
  }

  def executeCommit(): Unit = {

    val index = sgitReader.getIndex
    if (index.isEmpty) printer.improperSgitRepository()
    else {
      val lastCommitHash = sgitReader.getLastCommitHash
      val blobsLastCommit = commitHelper.getBlobsLastCommit(sgitReader.getCommit(lastCommitHash.getOrElse("")))
      val nbFilesChanged = commitHelper.nbFilesChangedSinceLastCommit(index.get, blobsLastCommit)
      val branch = sgitReader.getCurrentBranch
      //if nothing to commit
      if (nbFilesChanged.isEmpty)
        printer.nothingToCommit(branch)
      else {
        //if commit needed
        printer.askEnterMessageCommits()
        val message = inputManager.retrieveUserCommitMessage()
        val commit = commitHelper.commit(lastCommitHash, index.get, message)
        sgitWriter.saveCommit(commit,branch)
        printer.commitCreatedMessage(branch, message, nbFilesChanged.get)
      }
    }
  }

  def executeStatus(): Unit = {
    val index = sgitReader.getIndex
    if (index.isEmpty) printer.improperSgitRepository()
    else {
      //retrieve data
      val branch = sgitReader.getCurrentBranch
      val workspace = workspaceReader.getAllBlobsOfWorkspace()

      val lastCommitHash = sgitReader.getLastCommitHash.getOrElse("")
      val lastCommit = commitHelper.getBlobsLastCommit(sgitReader.getCommit(lastCommitHash))

      //process
      val untrackedFiles = statusHelper.getUntrackedFiles(workspace, index.get)
      val notStaged = statusHelper.getChangesNotStagedForCommit(index.get, workspace)
      val toBeCommitted = statusHelper.getChangesToBeCommitted(index.get, lastCommit)
      var printed = false
      //print
      if (branch.nonEmpty)
        printer.branch(branch.get)

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
  }

  def executeLog(): Unit = {
    //the log are available only on the current branch
    val branch = sgitReader.getCurrentBranch
    if (branch.nonEmpty) {
      val logFile = sgitReader.getLog(branch.get)
      if (logFile.isEmpty) printer.improperSgitRepository()
      else {
        val logs = logHelper.retrieveAllCommits(logFile.get)
        if (logs.isEmpty)
          printer.noLog(branch.get)
        else
          printer.displayAllCommits(logs, branch.get)
      }
    } else printer.notOnBranch()
  }


  def executeCheckout(switchTo: String): Unit = {
    // We retrieve the hash commit that corresponds to the switch
    var hashCommit: String = ""
    var head: String = switchTo
    val isCheckoutBranch = sgitReader.isExistingBranch(switchTo)
    if (isCheckoutBranch)
      hashCommit = sgitReader.getLastCommitOfBranch(switchTo).getOrElse("")
    else {
      if (sgitReader.isExistingTag(switchTo)) {
        hashCommit = sgitReader.getCommitTag(switchTo).getOrElse("")
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
      if (index.isEmpty) printer.improperSgitRepository()
      else {
        val checkoutBlobs = commitHelper.getBlobsOfCommit(sgitReader.getCommit(hashCommit).get)

        //if it exists files modified since the switch commit but not committed => switch impossible
        val lastCommitHash = sgitReader.getLastCommitHash
        val lastCommit = commitHelper.getBlobsLastCommit(sgitReader.getCommit(lastCommitHash.getOrElse("")))
        val checkoutNotAllowedOn = checkoutHelper.checkoutNotAllowedOn(lastCommit, index.get, checkoutBlobs)
        if (checkoutNotAllowedOn.nonEmpty)
          printer.notAllowedCheckout(checkoutNotAllowedOn)
        else {
          switch(head, isCheckoutBranch, index.get, checkoutBlobs)
          if(isCheckoutBranch)
            printer.checkoutBranch(head)
          else
            printer.detachedHead()
        }
      }
    }
  }

  private def switch(head: String, isCheckoutBranch: Boolean, currentIndex: List[EntryTree], checkoutBlobs: List[EntryTree]): Unit = {
    sgitWriter.setHead(head, isCheckoutBranch)
    sgitWriter.updateIndex(checkoutBlobs)
    val toDelete = checkoutHelper.findFilesToDelete(currentIndex, checkoutBlobs)
    val toCreate = checkoutHelper.findFilesToCreate(currentIndex, checkoutBlobs)
    workspaceReader.updateWorkspace(toDelete, toCreate)
  }

  def executeDiff(): Unit = {
    val workspace = workspaceReader.getAllBlobsOfWorkspace()
    val index = sgitReader.getIndex
    if (index.isEmpty) printer.improperSgitRepository()
    else {
      val notStaged = statusHelper.getChangesNotStagedForCommit(index.get, workspace)
      if (notStaged.nonEmpty) {
        val (modifiedNotStaged, deletedNotStaged) = notStaged.get

        modifiedNotStaged.map(x => {
          val absPath = PathHelper.getAbsolutePathOfFile(x.path)
          val oldContent = sgitReader.getContentOfObjectInString(x.hash).getOrElse("").split("\n").toList
          val newContent = workspaceReader.getContentOfFile(absPath)
          printer.printSingleDiff(x.path, diffHelper.diff(oldContent, newContent))
        })

        deletedNotStaged.map(x => {
          val oldContent = sgitReader.getContentOfObjectInString(x.hash).getOrElse("").split("\n").toList
          printer.printSingleDiff(x.path, diffHelper.diff(oldContent, List[String]()))
        })
      }
    }
  }


}

