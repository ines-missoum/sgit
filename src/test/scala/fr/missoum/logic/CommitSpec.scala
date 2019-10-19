package fr.missoum.logic

import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class CommitSpec extends FlatSpec with Matchers with IdiomaticMockito {

  behavior of "getCommitFromContent"

  it should "create a commit that corresponds to the structure of the content" in {
    //given
    val commit = new Commit("parent","myHash","treeHash","date","me","myMessage")
    val content = commit.buildContent
    //when
    val result = Commit.getCommitFromContent(content,"myHash")
    //then
    result.hash shouldBe commit.hash
    result.hashParentCommit shouldBe commit.hashParentCommit
    result.treeHash shouldBe commit.treeHash
  }

  behavior of "getCommitFromToString"

  it should "create a commit that corresponds to the structure of the toString" in {
    //given
    val commit = new Commit("parent","myHash","treeHash","date","me","myMessage")
    val toString = commit.toString
    //when
    val result = Commit.getCommitFromToString(toString)
    //then
    result.toString shouldBe commit.toString
  }
}
