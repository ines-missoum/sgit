package fr.missoum

import fr.missoum.utils.io.printers.ConsolePrinter
import fr.missoum.utils.io.readers.SgitReader
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
    mockReader.isExistingSgitFolder() was called
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
    mockReader.isExistingSgitFolder() returns false
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
    mockReader.isExistingSgitFolder() returns true
    //when
    objectTested.executeInit()
    //then
    mockWriter.createSgitRepository() wasNever called
    mockPrinter.sgitFolderAlreadyExists() was called
  }

}
