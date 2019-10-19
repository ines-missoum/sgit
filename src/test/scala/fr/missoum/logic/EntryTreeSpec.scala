package fr.missoum.logic

import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class EntryTreeSpec extends FlatSpec with Matchers with IdiomaticMockito{

  behavior of "apply"

  it should "create the corresponding entryTree" in {
    //given
    val line ="blob hash123456789 this/is/a/path"
    //when
    val result = EntryTree.apply(line)
    //then
    result.hash shouldBe "hash123456789"
    result.path shouldBe "this/is/a/path"
    result.entryType shouldBe EntryTree.BlobType
  }
}
