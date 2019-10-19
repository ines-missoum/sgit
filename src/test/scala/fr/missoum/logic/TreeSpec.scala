package fr.missoum.logic

import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class TreeSpec extends FlatSpec with Matchers with IdiomaticMockito {
  behavior of "apply from string"

  it should "create the corresponding tree" in {
    //given
    val line = "tree hash123456789 this/is/a/path"
    //when
    val result = Tree.apply(line)
    //then
    result.hash shouldBe "hash123456789"
    result.path shouldBe "this/is/a/path"
    result.entryType shouldBe EntryTree.TreeType
  }
}
