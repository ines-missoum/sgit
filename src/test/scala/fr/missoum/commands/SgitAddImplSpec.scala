package fr.missoum.commands

import fr.missoum.logic.{Blob, EntryTree}
import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class SgitAddImplSpec extends FlatSpec with Matchers with IdiomaticMockito {

  behavior of "recAdd function"

  it should "returns the index updated" in {
    //given
    val fakeIndex = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("hashBlob2", "pathBlob2"), Blob("hashBlob3", "pathBlob3"))
    //we want to delete blob1, modified blob2 and add a new blob4
    val fakeBlobsToAdd = List[EntryTree](Blob("", "pathBlob1"), Blob("newHashBlob2", "pathBlob2"), Blob("hashBlob4", "pathBlob4"))
    val classTested = SgitAddImpl
    //when
    val result = classTested.recAdd(fakeBlobsToAdd, fakeIndex, List[EntryTree]())

    //then
    val resultExpected = List[EntryTree](Blob("hashBlob3", "pathBlob3"), Blob("newHashBlob2", "pathBlob2"), Blob("hashBlob4", "pathBlob4"))
    result._1 should have length 3
    result._1 should contain(resultExpected(0))
    result._1 should contain(resultExpected(1))
    result._1 should contain(resultExpected(2))
  }

  it should "returns the list of blobs that have been created or modified" in {
    //given
    val fakeIndex = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("hashBlob2", "pathBlob2"), Blob("hashBlob3", "pathBlob3"))
    //we want to delete blob1, modified blob2 and add a new blob4
    val fakeBlobsToAdd = List[EntryTree](Blob("", "pathBlob1"), Blob("newHashBlob2", "pathBlob2"), Blob("hashBlob4", "pathBlob4"))
    val classTested = SgitAddImpl
    //when
    val result = classTested.recAdd(fakeBlobsToAdd, fakeIndex, List[EntryTree]())

    //then
    val resultExpected = List[EntryTree](Blob("newHashBlob2", "pathBlob2"), Blob("hashBlob4", "pathBlob4"))
    result._2 should have length 2
    result._2 should contain(resultExpected(0))
    result._2 should contain(resultExpected(1))
  }

}
