package fr.missoum

import fr.missoum.commands.{SgitCheckout, SgitCommit, SgitLog, SgitStatus}
import fr.missoum.logic.{Blob, Commit, EntryTree}
import fr.missoum.utils.io.inputs.UserInput
import fr.missoum.utils.io.printers.ConsolePrinter
import fr.missoum.utils.io.readers.SgitReader
import fr.missoum.utils.io.workspace.WorkspaceManager
import fr.missoum.utils.io.writers.SgitWriter
import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class CommandExecutorSpec extends FlatSpec with Matchers with IdiomaticMockito {

  behavior of "The execution of init"

  it should "check if a sgit repository already exists" in {
    //given
    val mockReader = mock[SgitReader]
    val mockWriter = mock[SgitWriter]
    val mockPrinter = mock[ConsolePrinter]
    val objectTested = CommandExecutorImpl
    objectTested.sgitReader = mockReader
    objectTested.sgitWriter = mockWriter
    objectTested.printer = mockPrinter
    //when
    objectTested.executeInit()
    //then
    mockReader.isExistingSgitFolder was called
  }

  it should "create a new sgit repository if it not already exists and inform user" in {
    //given
    val mockReader = mock[SgitReader]
    val mockWriter = mock[SgitWriter]
    val mockPrinter = mock[ConsolePrinter]
    val objectTested = CommandExecutorImpl
    objectTested.sgitReader = mockReader
    objectTested.sgitWriter = mockWriter
    objectTested.printer = mockPrinter
    mockReader.isExistingSgitFolder returns false
    //when
    objectTested.executeInit()
    //then
    mockWriter.createSgitRepository() was called
    mockPrinter.sgitFolderCreated() was called

  }

  it should "only inform user if sgit repository already exists" in {
    //given
    val mockReader = mock[SgitReader]
    val mockWriter = mock[SgitWriter]
    val mockPrinter = mock[ConsolePrinter]
    val objectTested = CommandExecutorImpl
    objectTested.sgitReader = mockReader
    objectTested.sgitWriter = mockWriter
    objectTested.printer = mockPrinter
    mockReader.isExistingSgitFolder returns true
    //when
    objectTested.executeInit()
    //then
    mockWriter.createSgitRepository() wasNever called
    mockPrinter.sgitFolderAlreadyExists() was called
  }

  behavior of "The execution of commit"

  it should "not commit anything if there's nothing to commit" in {
    //given
    val mockReader = mock[SgitReader]
    val mockCommitHelper = mock[SgitCommit]
    val mockInputManager = mock[UserInput]
    val mockWriter = mock[SgitWriter]
    val mockWorkspaceHelper = mock[WorkspaceManager]
    val mockPrinter = mock[ConsolePrinter]
    val mockStatusHelper = mock[SgitStatus]

    val objectTested = CommandExecutorImpl
    objectTested.sgitReader = mockReader
    objectTested.commitHelper = mockCommitHelper
    objectTested.inputManager = mockInputManager
    objectTested.sgitWriter = mockWriter
    objectTested.statusHelper = mockStatusHelper
    objectTested.workspaceReader = mockWorkspaceHelper
    objectTested.printer = mockPrinter
    //when
    mockReader.isExistingCommitOnCurrentBranch returns true
    mockReader.getIndex returns List(Blob("blob hash path"))
    mockCommitHelper.getBlobsLastCommit(false) returns List(Blob("blob hash path"))
    mockCommitHelper.nbFilesChangedSinceLastCommit(List(Blob("blob hash path")), List(Blob("blob hash path"))) returns None
    mockReader.getCurrentBranch returns "master"
    objectTested.executeCommit()
    //then
    mockPrinter.nothingToCommit("master") was called

  }

  it should "commit index if there's changes since last commit" in {
    //given
    val mockReader = mock[SgitReader]
    val mockCommitHelper = mock[SgitCommit]
    val mockInputManager = mock[UserInput]
    val mockWriter = mock[SgitWriter]
    val mockWorkspaceHelper = mock[WorkspaceManager]
    val mockPrinter = mock[ConsolePrinter]
    val mockStatusHelper = mock[SgitStatus]

    val objectTested = CommandExecutorImpl
    objectTested.sgitReader = mockReader
    objectTested.commitHelper = mockCommitHelper
    objectTested.inputManager = mockInputManager
    objectTested.sgitWriter = mockWriter
    objectTested.statusHelper = mockStatusHelper
    objectTested.workspaceReader = mockWorkspaceHelper
    objectTested.printer = mockPrinter
    //when
    val fakeIndex = List(Blob("blob hash path"), Blob("blob hash2 path/file.txt"))
    val fakeCommit = Commit("hash", "treeHash", "my message")
    mockReader.isExistingCommitOnCurrentBranch returns true
    mockReader.getIndex returns List(Blob("blob hash path"), Blob("blob hash2 path/file.txt"))
    mockCommitHelper.getBlobsLastCommit(false) returns List(Blob("blob hash path"))
    mockCommitHelper.nbFilesChangedSinceLastCommit(fakeIndex, List(Blob("blob hash path"))) returns Some(1)
    mockReader.getCurrentBranch returns "master"
    mockInputManager.retrieveUserCommitMessage() returns "my message"
    mockCommitHelper.commit(isFirstCommit = false, "master", fakeIndex, "my message") returns fakeCommit
    objectTested.executeCommit()
    //then
    mockWriter.saveCommit(fakeCommit, "master")
    mockPrinter.commitCreatedMessage("master", "my message", 1)
  }

  behavior of "The execution of log"

  it should "not log anything if there's no commit" in {
    //given
    val mockReader = mock[SgitReader]
    val mockLogHelper = mock[SgitLog]
    val mockPrinter = mock[ConsolePrinter]
    val objectTested = CommandExecutorImpl
    objectTested.sgitReader = mockReader
    objectTested.logHelper = mockLogHelper
    objectTested.printer = mockPrinter
    //when
    mockReader.getLog() returns "log"
    mockReader.getCurrentBranch returns "master"
    mockLogHelper.retrieveAllCommits("log") returns List[Commit]()
    objectTested.executeLog()
    //then
    mockPrinter.noLog("master")
  }

  it should "log when there are commits" in {
    //given
    val mockReader = mock[SgitReader]
    val mockLogHelper = mock[SgitLog]
    val mockPrinter = mock[ConsolePrinter]
    val objectTested = CommandExecutorImpl
    objectTested.sgitReader = mockReader
    objectTested.logHelper = mockLogHelper
    objectTested.printer = mockPrinter
    //when
    mockReader.getLog() returns "log"
    mockReader.getCurrentBranch returns "master"
    mockLogHelper.retrieveAllCommits("log") returns List(Commit("hash1", "hash2", "message"))
    objectTested.executeLog()
    //then
    mockPrinter.displayAllCommits(List(Commit("hash1", "hash2", "message")), "master")
  }

  behavior of "The creation of tag"

  it should "check if a tag of this name already exist" in {
    //given
    val mockReader = mock[SgitReader]
    val mockWriter = mock[SgitWriter]
    val objectTested = CommandExecutorImpl
    objectTested.sgitReader = mockReader
    objectTested.sgitWriter = mockWriter
    //when
    objectTested.executeCreateNewTag("myTag")
    //then
    mockReader.isExistingTag("myTag")


  }

  behavior of "The creation of branch"

  it should "check if a branch of this name already exist" in {

    //given
    val mockReader = mock[SgitReader]
    val mockWriter = mock[SgitWriter]
    val objectTested = CommandExecutorImpl
    objectTested.sgitReader = mockReader
    objectTested.sgitWriter = mockWriter
    //when
    objectTested.executeCreateNewBranch("myBranch")
    //then
    mockReader.isExistingBranch("myBranch")
  }

  behavior of "The checkout"

  it should "not execute checkout when switch doesn't exist" in {
    //given
    val mockReader = mock[SgitReader]
    val mockWriter = mock[SgitWriter]
    val mockCommitHelper = mock[SgitCommit]
    val mockCheckoutHelper = mock[SgitCheckout]
    val mockPrinter = mock[ConsolePrinter]
    val objectTested = CommandExecutorImpl
    objectTested.sgitReader = mockReader
    objectTested.sgitWriter = mockWriter
    objectTested.checkoutHelper = mockCheckoutHelper
    objectTested.commitHelper = mockCommitHelper
    objectTested.printer = mockPrinter
    //when
    objectTested.executeCheckout("switch")
    mockReader.isExistingBranch("switch") returns false
    mockReader.isExistingCommit("switch") returns false
    mockReader.isExistingTag("switch") returns false
    //then
    mockPrinter.notExistingSwitch("switch") was called
  }
  it should "not execute checkout when modified files are not committed" in {
    //given
    val mockReader = mock[SgitReader]
    val mockWriter = mock[SgitWriter]
    val mockCommitHelper = mock[SgitCommit]
    val mockCheckoutHelper = mock[SgitCheckout]
    val mockPrinter = mock[ConsolePrinter]
    val objectTested = CommandExecutorImpl
    objectTested.sgitReader = mockReader
    objectTested.sgitWriter = mockWriter
    objectTested.checkoutHelper = mockCheckoutHelper
    objectTested.commitHelper = mockCommitHelper
    objectTested.printer = mockPrinter
    //when
    mockReader.isExistingBranch("switch") returns true
    mockReader.isExistingCommit("switch") returns false
    mockReader.isExistingTag("switch") returns false
    mockReader.getLastCommitOfBranch("switch") returns "hashCommit"
    mockReader.getIndex returns List[EntryTree]()
    mockReader.getCommit("hashCommit") returns Commit("hash", "treeHash", "message")
    mockCommitHelper.getBlobsOfCommit(Commit("hash", "treeHash", "message")) returns List[EntryTree]()
    mockReader.isExistingCommitOnCurrentBranch returns true
    mockCommitHelper.getBlobsLastCommit(false) returns List[EntryTree]()
    mockCheckoutHelper.checkoutNotAllowedOn(List[EntryTree](), List[EntryTree](), List[EntryTree]()) returns List[String]("path/file.txt")

    objectTested.executeCheckout("switch")
    //then
    mockPrinter.notAllowedCheckout(List[String]("path/file.txt")) was called

  }

  it should "change the head, the index and the workspace" in {

  }

  behavior of "The diff"

  it should "retrieve the right files to update" in {
    //given
    val mockReader = mock[SgitReader]
    val mockWorkspace = mock[WorkspaceManager]
    val mockStatus = mock[SgitStatus]
    val objectTested = CommandExecutorImpl
    objectTested.sgitReader = mockReader
    objectTested.workspaceReader = mockWorkspace
    objectTested.statusHelper = mockStatus

    //when
    val fakeList = List[EntryTree]()
    mockWorkspace.getAllBlobsOfWorkspace() returns fakeList
    mockReader.getIndex returns fakeList
    mockStatus.getChangesNotStagedForCommit(fakeList, fakeList) returns None
    objectTested.executeDiff()

    //then
    mockStatus.getChangesNotStagedForCommit(List[EntryTree](), List[EntryTree]()) was called
  }


}
