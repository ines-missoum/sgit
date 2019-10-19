package fr.missoum.commands

import fr.missoum.logic.{Blob, EntryTree}
import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class SgitStatusImplSpec extends FlatSpec with Matchers with IdiomaticMockito {

  behavior of "getChangesToBeCommitted function"

  it should "returns the right groups of to be committed" in {
    //given
    val fakeIndex = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("hashBlob2", "pathBlob2"), Blob("hashBlob3", "pathBlob3"))
    val fakeCommit = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("newHashBlob2", "pathBlob2"), Blob("hashBlob4", "pathBlob4"))
    //when
    val result = SgitStatusImpl.getChangesToBeCommitted(fakeIndex,fakeCommit)
    //then
    result.get._1 shouldBe (List[EntryTree](Blob("hashBlob3", "pathBlob3")))
    result.get._2 shouldBe (List[EntryTree](Blob("hashBlob2", "pathBlob2")))
    result.get._3 shouldBe (List[EntryTree](Blob("hashBlob4", "pathBlob4")))
  }

  it should "returns None when nothing to be committed " in {
    //given
    val fakeIndex = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("hashBlob2", "pathBlob2"), Blob("hashBlob3", "pathBlob3"))
    val fakeCommit = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("hashBlob2", "pathBlob2"), Blob("hashBlob3", "pathBlob3"))
    //when
    val result = SgitStatusImpl.getChangesToBeCommitted(fakeIndex,fakeCommit)
    //then
    result shouldBe None
  }

  behavior of "getChangesNotStagedForCommit function"

  it should "returns the right groups of not staged for commit" in {
    //given
    val fakeIndex = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("hashBlob2", "pathBlob2"), Blob("hashBlob3", "pathBlob3"))
    val fakeWorkspace = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("newHashBlob2", "pathBlob2"), Blob("hashBlob4", "pathBlob4"))
    //when
    val result = SgitStatusImpl.getChangesNotStagedForCommit(fakeIndex,fakeWorkspace)
    //then
    result.get._1 shouldBe (List[EntryTree](Blob("hashBlob2", "pathBlob2")))
    result.get._2 shouldBe (List[EntryTree](Blob("hashBlob3", "pathBlob3")))

  }

  it should "returns None when nothing to stage " in {
    //given
    val fakeIndex = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("hashBlob2", "pathBlob2"), Blob("hashBlob3", "pathBlob3"))
    val fakeWorkspace = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("hashBlob2", "pathBlob2"), Blob("hashBlob3", "pathBlob3"))
    //when
    val result = SgitStatusImpl.getChangesNotStagedForCommit(fakeIndex,fakeWorkspace)
    //then
    result shouldBe None
  }

  behavior of "getUntrackedFiles function"

  it should "returns the right list of untracked" in {
    //given
    val fakeIndex = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("hashBlob2", "pathBlob2"), Blob("hashBlob3", "pathBlob3"))
    val fakeWorkspace = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("newHashBlob2", "pathBlob2"), Blob("hashBlob4", "pathBlob4"))
    //when
    val result = SgitStatusImpl.getUntrackedFiles(fakeWorkspace,fakeIndex)
    //then
    result.get shouldBe (List[EntryTree](Blob("hashBlob4", "pathBlob4")))


  }

  it should "returns None when nothing untracked " in {
    //given
    val fakeIndex = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("hashBlob2", "pathBlob2"), Blob("hashBlob3", "pathBlob3"))
    val fakeWorkspace = List[EntryTree](Blob("hashBlob1", "pathBlob1"), Blob("hashBlob2", "pathBlob2"), Blob("hashBlob3", "pathBlob3"))
    //when
    val result = SgitStatusImpl.getUntrackedFiles(fakeWorkspace,fakeIndex)
    //then
    result shouldBe None
  }

}
