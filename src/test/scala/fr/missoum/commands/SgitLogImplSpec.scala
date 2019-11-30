package fr.missoum.commands

import fr.missoum.logic.Commit
import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class SgitLogImplSpec extends FlatSpec with Matchers with IdiomaticMockito {

  behavior of "retrieveAllCommits function"

  it should "returns the list of commits" in {
    //given
    val content = "000000000000000 e6964fb56b19ccc user1" +
      "\nSat-Oct-19-13:53:33 first commit" +
      "\ne6964fb56b19ccc 4a90e8b3e683dd7545a949 user2" +
      "\nSat-Oct-19-13:54:55 commit2"
    //when
    val result = SgitLogImpl.retrieveAllCommits(content)
    val resultExpected2 = new Commit("000000000000000", "e6964fb56b19ccc", "", "Sat Oct 19 13:53:33", "user1", "first commit")
    val resultExpected1 = new Commit("e6964fb56b19ccc", "4a90e8b3e683dd7545a949", "", "Sat Oct 19 13:54:55", "user2", "commit2")
    //then
    result should have length 2
    result(0).hashParentCommit shouldBe resultExpected1.hashParentCommit
    result(0).hash shouldBe resultExpected1.hash
    result(0).date shouldBe resultExpected1.date
    result(0).author shouldBe resultExpected1.author
    result(1).hashParentCommit shouldBe resultExpected2.hashParentCommit
    result(1).hash shouldBe resultExpected2.hash
    result(1).date shouldBe resultExpected2.date
    result(1).author shouldBe resultExpected2.author


  }
}
