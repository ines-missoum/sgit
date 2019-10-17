package fr.missoum.commands

import fr.missoum.logic.{Blob, EntryTree}
import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class SgitCheckoutImplSpec extends FlatSpec with Matchers with IdiomaticMockito {

  behavior of "checkoutNotAllowedOn function"

  it should "return an empty list when checkout allowed" in {
    //given
    val fakeIndex = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("hashBlob2", "pathBlob2"), Blob("hashBlob3", "pathBlob3"))
    val fakeCheckout = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("hashBlob4", "pathBlob4"))
    val fakeLastCommit = List[EntryTree](Blob("hashBlob1", "pathBlob1"))
    val classTested = SgitCheckoutImpl
    //when
    val result = classTested.checkoutNotAllowedOn(fakeLastCommit, fakeIndex, fakeCheckout)
    //then
    result should have length 0
  }

  it should "return a list of all the path files that make the checkout impossible" in {
    //given
    val fakeIndex = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("hashBlob2", "pathBlob2"), Blob("hashBlob3", "pathBlob3"))
    val fakeCheckout = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("newHashBlob2", "pathBlob2"))
    val fakeLastCommit = List[EntryTree](Blob("hashBlob1", "pathBlob1"))
    val classTested = SgitCheckoutImpl
    //when
    val result = classTested.checkoutNotAllowedOn(fakeLastCommit, fakeIndex, fakeCheckout)
    //then
    result should have length 1
    result should contain("pathBlob2")
  }

  behavior of "findFilesToDelete function"

  it should "return the list of path blobs that should be deleted of the workspace when checkout" in {
    //given
    val fakeIndex = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("hashBlob2", "pathBlob2"), Blob("hashBlob3", "pathBlob3"))
    val fakeCheckout = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("newHashBlob2", "pathBlob2"),Blob("hashBlob4", "pathBlob4"))
    val classTested = SgitCheckoutImpl
    //when
    val result = classTested.findFilesToDelete(fakeIndex, fakeCheckout)
    val resultExpected = List[EntryTree](Blob("hashBlob2", "pathBlob2"), Blob("hashBlob3", "pathBlob3"))
    //then
    result should have length 2
    result should contain(resultExpected(0))
    result should contain(resultExpected(1))
  }

  behavior of "findFilesToCreate function"

  it should "return the list of path blobs that should be created of the workspace when checkout" in {
    //given
    val fakeIndex = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("hashBlob2", "pathBlob2"), Blob("hashBlob3", "pathBlob3"))
    val fakeCheckout = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("newHashBlob2", "pathBlob2"), Blob("hashBlob4", "pathBlob4"))
    val classTested = SgitCheckoutImpl
    //when
    val result = classTested.findFilesToCreate(fakeIndex, fakeCheckout)
    val resultExpected = List[EntryTree](Blob("newHashBlob2", "pathBlob2"), Blob("hashBlob4", "pathBlob4"))
    //then
    result should have length 2
    result should contain(resultExpected(0))
    result should contain(resultExpected(1))
  }
}
