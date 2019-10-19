package fr.missoum.utils.io.readers

import java.io._

import fr.missoum.logic.Commit
import fr.missoum.utils.helpers.PathHelper
import fr.missoum.utils.io.writers.SgitWriterImpl
import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class SgitReaderImplSpec extends FlatSpec with Matchers with IdiomaticMockito {

  private def setUpSgitRepository(): Unit = SgitWriterImpl.createSgitRepository()

  private def cleanSgitRepository(): Unit = deleteRecursively(new File(PathHelper.SgitRepositoryName))

  private def deleteRecursively(file: File): Unit = if (file.isDirectory) {
    file.listFiles.map(deleteRecursively(_))
    file.delete()
  } else file.delete


  behavior of "isExistingCommit method"

  it should "returns false when the object exists but is not a commit" in {
    //given
    setUpSgitRepository()
    val path = PathHelper.ObjectDirectory + File.separator + "be"
    new File(path).mkdir()
    new File(path + File.separator + "12sqg5er8869").createNewFile
    //when
    val result = SgitReaderImpl.isExistingCommit("be12sqg5er8869")
    //then
    result shouldBe false
    //clean
    cleanSgitRepository()
  }

  it should "returns false when the object doesn't exists" in {
    //given
    setUpSgitRepository()
    //when
    val result = SgitReaderImpl.isExistingCommit("123456789")
    //then
    result shouldBe false
    //clean
    cleanSgitRepository()
  }

  it should "returns true when the object exists and is a commit" in {

    //given
    setUpSgitRepository()
    val pathDir = PathHelper.ObjectDirectory + File.separator + "be"
    val pathFile = PathHelper.ObjectDirectory + File.separator + "be" + File.separator + "12sqg5er8869"
    new File(pathDir).mkdir()
    new File(pathFile).createNewFile
    val commit = Commit("parentHash", "12sqg5er8869", "treeHash", "date", "author", "myMessage")
    val content = commit.buildContent
    new PrintWriter(pathFile) {
      write(content)
      close()
    }
    //when
    val result = SgitReaderImpl.isExistingCommit("be12sqg5er8869")
    //then
    result shouldBe true
    //clean
    cleanSgitRepository()
  }

  behavior of "getCommitTag method"

  it should "returns the hash of the tag when exists" in {
    //given
    setUpSgitRepository()
    val fakeTag = "myTag"
    val path = PathHelper.TagsDirectory + File.separator + fakeTag
    val hashExpected = Some("e2kjgks8856qsqfd")
    new File(path).createNewFile
    new PrintWriter(path) {
      write(hashExpected.get)
      close()
    }
    //when
    val result = SgitReaderImpl.getCommitTag(fakeTag)
    //then
    result shouldBe hashExpected
    //clean
    cleanSgitRepository()
  }

  behavior of "getLastCommitOfBranch method"

  it should "returns the hash of the branch when exists" in {
    //given
    setUpSgitRepository()
    val fakeBranch = "myBranch"
    val path = PathHelper.BranchesDirectory + File.separator + fakeBranch
    val hashExpected = Some("e2kjgks8856qsqfd")
    new File(path).createNewFile
    new PrintWriter(path) {
      write(hashExpected.get)
      close()
    }
    //when
    val result = SgitReaderImpl.getLastCommitOfBranch(fakeBranch)
    //then
    result shouldBe hashExpected
    //clean
    cleanSgitRepository()

  }
}
