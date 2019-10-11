package fr.missoum.utils.io.readers

import java.io._

import fr.missoum.utils.helpers.PathHelper
import fr.missoum.utils.io.writers.SgitWriterImpl
import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class SgitReaderImplSpec extends FlatSpec with Matchers with IdiomaticMockito {

  private def setUpSgitRepository = SgitWriterImpl.createSgitRepository()

  private def cleanSgitRepository = deleteRecursively(new File(PathHelper.SgitRepositoryName))

  private def deleteRecursively(file:File): Unit = if (file.isDirectory) file.listFiles.map(deleteRecursively(_)) else file.delete


  behavior of "isExistingCommit method"

  it should "returns true when a commit exists on the current branch" in {
    //given
    setUpSgitRepository
    new PrintWriter(PathHelper.BranchesDirectory+File.separator+"master") { write("file contents"); close }
    //when
    val result = SgitReaderImpl.isExistingCommit()
    //then
    result shouldBe true
    //clean
    cleanSgitRepository
  }

  it should "returns false when a commit do not exists on the current branch" in {
    //given
    setUpSgitRepository
    //when
    val result = SgitReaderImpl.isExistingCommit()
    //then
    result shouldBe false
    //clean
    cleanSgitRepository
  }


}
