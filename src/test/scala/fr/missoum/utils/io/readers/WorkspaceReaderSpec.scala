package fr.missoum.utils.io.readers

import java.io.File

import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class WorkspaceReaderSpec extends FlatSpec with Matchers with IdiomaticMockito {

  behavior of "fileExists method"

  it should "returns true when file exists" in {
    //given
    val nameFile = "testFile"
    val file: File = new File(nameFile)
    file.createNewFile
    val classTested = WorkspaceReaderImpl
    //when
    val result = classTested.fileExists(nameFile)
    //then
    result shouldBe true

    //clean
    file.delete()

  }

  it should "returns false when file do not exists" in {

    //given
    val nameFile = "testFile"
    val classTested = WorkspaceReaderImpl
    //when
    val result = classTested.fileExists(nameFile)
    //then
    result shouldBe false

  }
}
