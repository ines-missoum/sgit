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

  behavior of "buildDiff function"
  it should "returns the correct list of differences that corresponds to the matrix" in {
    //given
    val content1 = List("a", "b", "c", "d", "a", "f")
    val content2 = List("a", "c", "b", "c", "f")
    val matrix = Array(
      Array(0, 0, 0, 0, 0, 0, 0),
      Array(0, 1, 1, 1, 1, 1, 1),
      Array(0, 1, 1, 2, 2, 2, 2),
      Array(0, 1, 2, 2, 2, 2, 2),
      Array(0, 1, 2, 3, 3, 3, 3),
      Array(0, 1, 2, 3, 3, 3, 4))
    val objectTested = SgitDiffImpl
    //when
    val result = objectTested.buildDiff(matrix, content1, content2)
    //then
    val resultExpected = List[String]("a", "+c", "b", "c", "-d", "-a", "f")
    result shouldBe resultExpected
  }

  behavior of "diff function"
  it should "returns the correct list of differences" in {
    //given
    val content1 = List("a", "b", "c", "d", "a", "f")
    val content2 = List("a", "c", "b", "c", "f")
    val objectTested = SgitDiffImpl
    //when
    val result = objectTested.diff(content1, content2)
    //then
    val resultExpected = List[String]("a", "+c", "b", "c", "-d", "-a", "f")
    result shouldBe resultExpected
  }

  it should "returns the correct list of differences when one of the file is empty" in {
    //given
    val content1 = List("a", "b", "c", "d", "a", "f")
    val content2 = List()
    val objectTested = SgitDiffImpl
    //when
    val resultRemoveAll = objectTested.diff(content1, content2)
    val resultAddAll = objectTested.diff(content2, content1)
    //then
    val removeAllExpected = List("-a", "-b", "-c", "-d", "-a", "-f")
    val addAllExpected = List("+a", "+b", "+c", "+d", "+a", "+f")
    resultRemoveAll shouldBe removeAllExpected
    resultAddAll shouldBe addAllExpected
  }
}
