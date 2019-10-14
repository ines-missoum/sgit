package fr.missoum

import java.io.File

import fr.missoum.utils.helpers.PathHelper
import fr.missoum.utils.io.printers.ConsolePrinter
import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class MainSpec extends FlatSpec with Matchers with IdiomaticMockito {

  private def setupSgitRepo(): Unit ={
    val nameFolder = PathHelper.SgitRepositoryName
    val file: File = new File(nameFolder)
    file.createNewFile
  }

  private def cleanSgitRepo(): Unit ={
    val nameFolder = PathHelper.SgitRepositoryName
    val file: File = new File(nameFolder)
    file.delete
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
    setupSgitRepo()
    val arg = Array("commit")
    val mockExecutor = mock[CommandExecutor]
    val classTested = Main
    classTested.executor = mockExecutor
    //when
    Main.main(arg)
    //then
    mockExecutor.executeCommit() was called

    //clean
    cleanSgitRepo()

  }

  it should "not execute commit command when first argument is commit and others arguments are invalid and sgit repository exists" in {

    //given
    setupSgitRepo()
    val arg = Array("commit","testCommand")
    val mockExecutor = mock[CommandExecutor]
    val mockPrinter = mock[ConsolePrinter]
    val classTested = Main
    classTested.executor = mockExecutor
    classTested.printer = mockPrinter
    //when
    Main.main(arg)
    //then
    mockExecutor.executeCommit wasNever  called
    mockPrinter.notValidArguments("commit","just 'commit'") was called

    //clean
    cleanSgitRepo()

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

}
