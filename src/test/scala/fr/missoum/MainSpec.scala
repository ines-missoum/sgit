package fr.missoum

import fr.missoum.utils.io.printers.ConsolePrinter
import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class MainSpec extends FlatSpec with Matchers with IdiomaticMockito {

  behavior of "Main class with no valid command as first args"

  it should "inform the user that's is not a valid command when existing sgit repository" in {
    //given
    val arg = Array("noValidCommand")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    mockExecutor.isCommandForbiddenHere() returns false
    //when
    Main.main(arg)
    //then
    mockPrinter.notValidCommand("noValidCommand") was called


  }
  behavior of "Main class with init first args"

  it should "execute init command when argument is init and sgit repository do not exists" in {
    //given
    val arg = Array("init")
    val mockExecutor = mock[CommandExecutor]
    val classTested = Main
    classTested.executor = mockExecutor
    //when
    Main.main(arg)
    //then
    mockExecutor.executeInit was called

  }

  it should "not execute init command when first argument is init and others arguments are invalid " in {

    //given
    val arg = Array("init","testCommand")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    //when
    Main.main(arg)
    //then
    mockExecutor.executeInit wasNever  called
    mockPrinter.notValidArguments("init","just 'init'") was called

  }

  behavior of "Main class with commit first args"

  it should "execute commit command when argument is commit and sgit repository exists" in {
    //given
    val arg = Array("commit")
    val mockExecutor = mock[CommandExecutor]
    val classTested = Main
    classTested.executor = mockExecutor
    mockExecutor.isCommandForbiddenHere() returns false
    //when
    Main.main(arg)
    //then
    mockExecutor.executeCommit() was called
  }

  it should "not execute commit command when first argument is commit and others arguments are invalid and sgit repository exists" in {

    //given
    val arg = Array("commit","testCommand")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    mockExecutor.isCommandForbiddenHere() returns false
    //when
    Main.main(arg)
    //then
    mockExecutor.executeCommit wasNever  called
    mockPrinter.notValidArguments("commit","just 'commit'") was called


  }

  it should "not execute commit sgit repository do not  exists" in {

    //given
    val arg = Array("commit","testCommand")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    mockExecutor.isCommandForbiddenHere() returns true
    //when
    Main.main(arg)
    //then
    mockExecutor.executeCommit() wasNever  called
    mockPrinter.notExistingSgitRepository() was called


  }

  behavior of "Main class with status first args"

  it should "execute status command when argument is status and sgit repository exists" in {
    //given
    val arg = Array("status")
    val mockExecutor = mock[CommandExecutor]
    val classTested = Main
    classTested.executor = mockExecutor
    mockExecutor.isCommandForbiddenHere() returns false
    //when
    Main.main(arg)
    //then
    mockExecutor.executeStatus() was called
  }

  it should "not execute status command when first argument is status and others arguments are invalid and sgit repository exists" in {

    //given
    val arg = Array("status","testCommand")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    mockExecutor.isCommandForbiddenHere() returns false
    //when
    Main.main(arg)
    //then
    mockExecutor.executeStatus() wasNever  called
    mockPrinter.notValidArguments("status","just 'status'") was called
  }

  it should "not execute status sgit repository do not exists" in {

    //given
    val arg = Array("status","testCommand")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    mockExecutor.isCommandForbiddenHere() returns true
    //when
    Main.main(arg)
    //then
    mockExecutor.executeStatus() wasNever  called
    mockPrinter.notExistingSgitRepository() was called

  }

  behavior of "Main class with log first args"

  it should "execute log command when argument is status and sgit repository exists" in {
    //given
    val arg = Array("log")
    val mockExecutor = mock[CommandExecutor]
    val classTested = Main
    classTested.executor = mockExecutor
    mockExecutor.isCommandForbiddenHere() returns false
    //when
    Main.main(arg)
    //then
    mockExecutor.executeLog() was called

  }

  it should "not execute log command when first argument is log and others arguments are invalid and sgit repository exists" in {

    //given
    val arg = Array("log","testCommand")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    mockExecutor.isCommandForbiddenHere() returns false
    //when
    Main.main(arg)
    //then
    mockExecutor.executeLog() wasNever  called
    mockPrinter.notValidArguments("log","just 'log'") was called

  }

  it should "not execute log when sgit repository do not exists" in {

    //given
    val arg = Array("log","testCommand")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    mockExecutor.isCommandForbiddenHere() returns true
    //when
    Main.main(arg)
    //then
    mockExecutor.executeLog() wasNever  called
    mockPrinter.notExistingSgitRepository() was called
  }

  behavior of "Main class with branch first args"

  it should "not execute creation of branch when sgit repository do not exists" in {

    //given
    val arg = Array("branch","nameBranch")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    mockExecutor.isCommandForbiddenHere() returns true
    //when
    Main.main(arg)
    //then
    mockExecutor.executeCreateNewBranch("nameBranch") wasNever called
    mockExecutor.executeGetAllBranchesAndTags() wasNever  called
    mockPrinter.notExistingSgitRepository() was called
  }

  it should "not execute printing of all branches and tags when sgit repository do not exists" in {

    //given
    val arg = Array("branch","-av")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    mockExecutor.isCommandForbiddenHere() returns true
    //when
    Main.main(arg)
    //then
    mockExecutor.executeCreateNewBranch("nameBranch") wasNever called
    mockExecutor.executeGetAllBranchesAndTags() wasNever  called
  }

  it should "execute a creation of branch when arguments are branch 'nameBranch' and sgit repository exists" in {
    //given
    val arg = Array("branch","name")
    val mockExecutor = mock[CommandExecutor]
    val classTested = Main
    classTested.executor = mockExecutor
    mockExecutor.isCommandForbiddenHere() returns false
    //when
    Main.main(arg)
    //then
    mockExecutor.executeCreateNewBranch("name") was called

  }

  it should "execute command to retrieve all tags and branches when arguments are branch -av and sgit repository exists" in {
    //given
    val arg = Array("branch", "-av")
    val mockExecutor = mock[CommandExecutor]
    val classTested = Main
    classTested.executor = mockExecutor
    mockExecutor.isCommandForbiddenHere() returns false
    //when
    Main.main(arg)
    //then
    mockExecutor.executeGetAllBranchesAndTags() was called

  }

  behavior of "Main class with tag first args"

  it should "not execute creation of tag when sgit repository do not exists" in {

    //given
    val arg = Array("tag","name")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    mockExecutor.isCommandForbiddenHere() returns true
    //when
    Main.main(arg)
    //then
    mockExecutor.executeCreateNewTag("name") wasNever called
    mockPrinter.notExistingSgitRepository() was called
  }

  it should "execute a creation of tag when arguments are tag 'name' and sgit repository exists" in {
    //given
    val arg = Array("tag","name")
    val mockExecutor = mock[CommandExecutor]
    val classTested = Main
    classTested.executor = mockExecutor
    mockExecutor.isCommandForbiddenHere() returns false
    //when
    Main.main(arg)
    //then
    mockExecutor.executeCreateNewTag("name") was called
  }

  behavior of "Main class with add first args"

  it should "not execute add command when sgit repository do not exists" in {

    //given
    val arg = Array("add","file")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    mockExecutor.isCommandForbiddenHere() returns true
    //when
    Main.main(arg)
    //then
    mockExecutor.executeAdd(List("file")) wasNever called
    mockPrinter.notExistingSgitRepository() was called
  }

  it should "not execute add command when first argument is add and no other args and sgit repository exists" in {

    //given
    val arg = Array("add")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    mockExecutor.isCommandForbiddenHere() returns false
    //when
    Main.main(arg)
    //then
    mockExecutor.executeAdd(List()) wasNever  called
    mockPrinter.notValidArguments("add","'add <file>' or 'add <file>*' ") was called
  }

  it should "execute add command when first argument is add and other args and sgit repository exists" in {

    //given
    val arg = Array("add","file1","file2","file3")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    mockExecutor.isCommandForbiddenHere() returns false
    //when
    Main.main(arg)
    //then
    mockExecutor.executeAdd(List("file1","file2","file3")) was  called
  }

  behavior of "Main class with checkout first args"

  it should "execute checkout when arguments are checkout 'branch' and sgit repository exists" in {
    //given
    val arg = Array("checkout", "myBranch")
    val mockExecutor = mock[CommandExecutor]
    val classTested = Main
    classTested.executor = mockExecutor
    mockExecutor.isCommandForbiddenHere() returns false
    //when
    Main.main(arg)
    //then
    mockExecutor.executeCheckout("myBranch") was called
  }

  it should "not execute checkout when sgit repository do not exists" in {

    //given
    val arg = Array("checkout","myBranch")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    mockExecutor.isCommandForbiddenHere() returns true
    //when
    Main.main(arg)
    //then
    mockExecutor.executeCheckout("myBranch") wasNever  called
    mockPrinter.notExistingSgitRepository() was called
  }

  behavior of "Main class with diff first args"

  it should "execute diff command when argument is diff and sgit repository exists" in {
    //given
    val arg = Array("diff")
    val mockExecutor = mock[CommandExecutor]
    val classTested = Main
    classTested.executor = mockExecutor
    mockExecutor.isCommandForbiddenHere() returns false
    //when
    Main.main(arg)
    //then
    mockExecutor.executeDiff() was called
  }

  it should "not execute diff command when first argument is diff and others arguments are invalid and sgit repository exists" in {

    //given
    val arg = Array("diff","testCommand")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    mockExecutor.isCommandForbiddenHere() returns false
    //when
    Main.main(arg)
    //then
    mockExecutor.executeDiff() wasNever  called
    mockPrinter.notValidArguments("diff","just 'diff'") was called
  }

  it should "not execute diff sgit repository do not exists" in {

    //given
    val arg = Array("diff","testCommand")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    mockExecutor.isCommandForbiddenHere() returns true
    //when
    Main.main(arg)
    //then
    mockExecutor.executeDiff() wasNever  called
    mockPrinter.notExistingSgitRepository() was called

  }


}
