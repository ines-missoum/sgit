package fr.missoum.commands

import fr.missoum.logic.{Blob, Commit, EntryTree, Tree}
import fr.missoum.utils.helpers.HashHelper
import fr.missoum.utils.io.readers.SgitReader
import fr.missoum.utils.io.writers.SgitWriter
import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class SgitCommitImplSpec extends FlatSpec with Matchers with IdiomaticMockito {

  behavior of "nbFilesChangedSinceLastCommit function"

  it should "returns 0 when nothing was created or modified" in {
    //given
    val objectTested = SgitCommitImpl
    val fakeList = List(Blob("blob hash path"), Blob("blob hash2 path/file.txt"))
    //when
    val result = objectTested.nbFilesChangedSinceLastCommit(fakeList, fakeList)
    //then
    result shouldBe None
  }

  it should "returns the number of modifications + creations when there are" in {

    //given
    val objectTested = SgitCommitImpl
    val fakeIndex = List(Blob("blob hash path"), Blob("blob hash2 path/file.txt"))
    val fakeCommitBlobs = List(Blob("blob hash path"))
    //when
    val result = objectTested.nbFilesChangedSinceLastCommit(fakeIndex, fakeCommitBlobs)
    //then
    result shouldBe Some(1)
  }

  behavior of "getBlobsLastCommit function"

  it should "returns an empty list when it's the first commit" in {
    //given
    val mockReader = mock[SgitReader]
    val objectTested = SgitCommitImpl
    objectTested.sgitReader = mockReader
    //when
    val result = objectTested.getBlobsLastCommit(None)
    //then
    result shouldBe List[EntryTree]()
  }

  it should "returns the list of blobs of the last commit when it exists" in {
    //given
    val mockReader = mock[SgitReader]
    val objectTested = SgitCommitImpl
    objectTested.sgitReader = mockReader
    //when
    val mockEntries1 = List[EntryTree](Blob("blob hash path"), Blob("blob hash2 path/file.txt"), Tree("hashTree5678", "path"))
    val mockEntries2 = List[EntryTree](Blob("blob hash3 path3"))
    mockReader.getContentOfObjectInEntries("hashTree1234") returns mockEntries1
    mockReader.getContentOfObjectInEntries("hashTree5678") returns mockEntries2
    val resultExpected = List[EntryTree](Blob("blob hash path"), Blob("blob hash2 path/file.txt"), Blob("blob hash3 path3"))
    val result = objectTested.getBlobsLastCommit(Some(Commit("hashParent1234", "hashTree1234", "myMessage")))
    //then
    result shouldBe resultExpected
  }

  behavior of "commit function"

  it should "creates the commit instance correctly when first commit" in {

    //given
    val mockReader = mock[SgitReader]
    val mockWriter = mock[SgitWriter]
    val objectTested = SgitCommitImpl
    objectTested.sgitReader = mockReader
    objectTested.sgitWriter = mockWriter
    //when
    val fakeIndex = List(Blob("blob hash path"), Blob("blob hash2 path/file.txt"))
    val expectedHash = HashHelper.hashFile("blob hash path\nblob hash2 path/file.txt")
    mockWriter.createObject("blob hash path\nblob hash2 path/file.txt")
    val result = objectTested.commit(None, fakeIndex: List[EntryTree], "MyMessage")

    val resultExpected = Commit("0000000000000000000000000000000000000000", expectedHash, "MyMessage")
    //then
    result.hashParentCommit shouldBe resultExpected.hashParentCommit
    result.hash shouldBe resultExpected.hash
    result.message shouldBe resultExpected.message
  }


}
