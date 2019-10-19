package fr.missoum.utils.io.readers

import java.io.{File, PrintWriter}

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
    mockReader.getContentOfObjectInString("12hashBlob1") returns Some("test")
    mockReader.getContentOfObjectInString("33hashBlob3") returns Some("test")
    classTested.updateWorkspace(fakeToDelete, fakeToCreate)

    //then
    file1 should exist
    file3 should exist
    file2 shouldNot exist
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
    mockReader.getContentOfObjectInString("12hashBlob1") returns Some("test")
    mockReader.getContentOfObjectInString("33hashBlob3") returns Some("test")
    classTested.updateWorkspace(fakeToDelete, fakeToCreate)

    //then
    new File("dir2") shouldNot exist
    //clean
    file1.delete()
    file3.delete()
    if (file2.exists()) file2.delete()
    dir1.delete()
    dir4.delete()
    new File("dir3").delete()
    cleanSgitRepository()
  }

  it should "give the right content to the files" in {

    pending
    // TODO
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
    val pathFolder1 = new File(PathHelper.ObjectDirectory + File.separator + "11")
    val pathFolder2 = new File(PathHelper.ObjectDirectory + File.separator + "12")
    val pathObj1 = new File(pathFolder1 + File.separator + "hashBlob1")
    val pathObj2 = new File(pathFolder2 + File.separator + "hashBlob1")
    new PrintWriter(pathObj1) {
      write("content")
      close()
    }
    //when
    classTested.updateWorkspace(fakeToDelete, fakeToCreate)

    //then
    new File("dir2") shouldNot exist
    //clean
    file1.delete()
    file3.delete()
    if (file2.exists()) file2.delete()
    dir1.delete()
    dir4.delete()
    new File("dir3").delete()
    cleanSgitRepository()
  }

  it should "not touch other files" in {
    //given
    setUpSgitRepository()
    val classTested = WorkspaceManagerImpl
    val mockReader = mock[SgitReader]
    classTested.reader = mockReader
    val file1 = new File("dir1/pathBlob1")
    val file2 = new File("dir2/pathBlob2")
    val file3 = new File("dir3/dir4/pathBlob3")
    val file4 = new File("dir3/dir4/pathBlob5")
    val dir1 = new File("dir1")
    dir1.mkdirs()
    val dir4 = new File("dir3/dir4")
    dir4.mkdirs()
    file1.createNewFile()
    file3.createNewFile()
    file4.createNewFile()
    val fakeToDelete = List[EntryTree](Blob("11hashBlob1", "dir1/pathBlob1"), Blob("22hashBlob2", "dir2/pathBlob2"))
    val fakeToCreate = List[EntryTree](Blob("12hashBlob1", "dir1/pathBlob1"), Blob("33hashBlob3", "dir3/dir4/pathBlob3"))
    //when
    mockReader.getContentOfObjectInString("12hashBlob1") returns Some("test")
    mockReader.getContentOfObjectInString("33hashBlob3") returns Some("test")
    classTested.updateWorkspace(fakeToDelete, fakeToCreate)

    //then
    file4 should exist
    //clean
    file1.delete()
    file3.delete()
    file4.delete()
    if (file2.exists()) file2.delete()
    dir1.delete()
    dir4.delete()
    new File("dir3").delete()
    cleanSgitRepository()
  }
}
