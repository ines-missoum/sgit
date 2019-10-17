package fr.missoum.utils.io.readers

import java.io.File

import fr.missoum.logic.{Blob, EntryTree}
import fr.missoum.utils.helpers.PathHelper
import fr.missoum.utils.io.workspace.WorkspaceManagerImpl
import fr.missoum.utils.io.writers.SgitWriterImpl
import org.mockito.IdiomaticMockito
import org.scalatest.{FlatSpec, Matchers}

class WorkspaceManagerImplSpec extends FlatSpec with Matchers with IdiomaticMockito {
  private def setUpSgitRepository(): Unit = SgitWriterImpl.createSgitRepository()

  private def cleanSgitRepository(): Unit = deleteRecursively(new File(PathHelper.SgitRepositoryName))

  private def deleteRecursively(file: File): Unit = if (file.isDirectory) {
    file.listFiles.map(deleteRecursively(_))
    file.delete()
  } else file.delete

  behavior of "fileExists method"

  it should "returns true when file exists" in {
    //given
    val nameFile = "testFile"
    val file: File = new File(nameFile)
    file.createNewFile
    val classTested = WorkspaceManagerImpl
    //when
    val result = classTested.fileExists(nameFile)
    //then
    result shouldBe true

    //clean
    file.delete()

  }

  it should "returns false when file do not exists" in {

    //given
    val nameFile = "testFile"
    val classTested = WorkspaceManagerImpl
    //when
    val result = classTested.fileExists(nameFile)
    //then
    result shouldBe false

  }

  behavior of "updateWorkspace method"

  it should "create and delete all the files in parameters" in {
    //given
    setUpSgitRepository()
    val classTested = WorkspaceManagerImpl
    val mockReader = mock[SgitReader]
    classTested.reader = mockReader
    val file1 = new File("dir1/pathBlob1")
    val file2 = new File("dir2/pathBlob2")
    val file3 = new File("dir3/dir4/pathBlob3")
    val dir1 = new File("dir1")
    dir1.mkdirs()
    val dir4 = new File("dir3/dir4")
    dir4.mkdirs()
    file1.createNewFile()
    file3.createNewFile()
    val fakeToDelete = List[EntryTree](Blob("11hashBlob1", "dir1/pathBlob1"), Blob("22hashBlob2", "dir2/pathBlob2"))
    val fakeToCreate = List[EntryTree](Blob("12hashBlob1", "dir1/pathBlob1"), Blob("33hashBlob3", "dir3/dir4/pathBlob3"))
    //when
    classTested.updateWorkspace(fakeToDelete, fakeToCreate)

    //then
    file1 should (exist)
    file3 should (exist)
    file2 shouldNot (exist)
    //clean
    file1.delete()
    file3.delete()
    if (file2.exists()) file2.delete()
    dir1.delete()
    dir4.delete()
    new File("dir3").delete()
    cleanSgitRepository()
  }
  it should "not forget to delete empty directories" in {

  }

  it should "give the right content to the files" in {

  }

  it should "not touch other files" in {

  }
}
