package fr.missoum.utils.helpers

import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class HashHelperSpec extends FlatSpec with Matchers with IdiomaticMockito {

  behavior of "Hashes"

  it should "be the same if same content" in {
    //given
    val classTested = HashHelper
    val content = "This is \n my content"
    //when
    val hash1 = classTested.hashFile(content)
    val hash2 = classTested.hashFile(content)
    //then
    hash1 shouldBe hash2
  }

  it should "be different if different content" in {
    //given
    val classTested = HashHelper
    val content1 = "This is \n my content"
    val content2 = "This is another content"
    //when
    val hash1 = classTested.hashFile(content1)
    val hash2 = classTested.hashFile(content2)
    //then
    hash1 should not be hash2
  }

}
