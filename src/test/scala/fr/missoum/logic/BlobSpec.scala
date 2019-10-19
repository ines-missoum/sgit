package fr.missoum.logic

import fr.missoum.utils.helpers.HashHelper
import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class BlobSpec extends FlatSpec with Matchers with IdiomaticMockito {

  behavior of "addContent"

  it should "add the same content in the blob and the corresponding hash" in {
    //given
    val content = "This is my content"
    val initialBlob = new Blob("blob", "hash", "path", None, None)
    //when
    val result = initialBlob.addContent(content)
    //then
    val resultExpected = new Blob("blob", HashHelper.hashFile(content), "path", Some(content), None)
    result.hash shouldBe resultExpected.hash
    result.path shouldBe resultExpected.path
    result.entryType shouldBe resultExpected.entryType
    result.listEntryTree shouldBe resultExpected.listEntryTree
    result.contentString shouldBe resultExpected.contentString
  }

  behavior of "newBlobWithContent"

  it should "create a blob with a content in the blob and the corresponding hash" in {
    //given
    val content = "This is my content"
    //when
    val result = Blob.newBlobWithContent(content, "path")
    //then
    val resultExpected = new Blob("blob", HashHelper.hashFile(content), "path", Some(content), None)
    result.hash shouldBe resultExpected.hash
    result.path shouldBe resultExpected.path
    result.entryType shouldBe resultExpected.entryType
    result.listEntryTree shouldBe resultExpected.listEntryTree
    result.contentString shouldBe resultExpected.contentString
  }
}
