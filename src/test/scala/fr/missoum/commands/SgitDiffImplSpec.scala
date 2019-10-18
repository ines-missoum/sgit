package fr.missoum.commands

import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class SgitDiffImplSpec extends FlatSpec with Matchers with IdiomaticMockito {
  behavior of "buildMatrix function"

  it should "returns the correct matrix" in {
    //given
    val content1 = List("a", "b", "c", "d", "a", "f")
    val content2 = List("a", "c", "b", "c", "f")
    val objectTested = SgitDiffImpl
    //when
    val result = objectTested.buildMatrix(content1, content2)
    //then
    val resultExpected = Array(
      Array(0, 0, 0, 0, 0, 0, 0),
      Array(0, 1, 1, 1, 1, 1, 1),
      Array(0, 1, 1, 2, 2, 2, 2),
      Array(0, 1, 2, 2, 2, 2, 2),
      Array(0, 1, 2, 3, 3, 3, 3),
      Array(0, 1, 2, 3, 3, 3, 4))
    result shouldBe resultExpected
  }
}
