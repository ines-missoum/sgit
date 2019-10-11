package fr.missoum.commands

import java.io.File

import fr.missoum.logic.{Blob, EntryTree}
import fr.missoum.utils.helpers.{HashHelper, PathHelper}
import fr.missoum.utils.io.readers.{SgitReaderImpl, WorkspaceReaderImpl}
import fr.missoum.utils.io.writers.SgitWriterImpl

import scala.annotation.tailrec

object SgitAdd {

  def getNotExistingFile(filesNames: Array[String]) =
    filesNames.filter(x => !WorkspaceReaderImpl.fileExists(System.getProperty("user.dir") + File.separator + x))

  def addAll(filesNames: Array[String]): Unit = {

    // we retrieve all the content of the index file in format of array of blobs
    val indexBlobs = SgitReaderImpl.getIndex().map(x => Blob(x))

    //we create a array of blobs from the array of files names
    val newFilesBlobs: Array[EntryTree] = filesNames.map(x => {
      val absolutePath = System.getProperty("user.dir") + File.separator + x
      val simplePath = PathHelper.getSimplePathOfFile(absolutePath)
      val content = SgitReaderImpl.getContentOfFile(absolutePath)
      Blob.NewBlobWithContent(content, simplePath)
    })

    //we add all files that need to
    recAdd(newFilesBlobs, indexBlobs)

  }

  @tailrec
  def recAdd(filesBlobs: Array[EntryTree], index: Array[EntryTree]): Unit = {
    //if no more files to deal with, we save the index
    if (filesBlobs.length == 0) SgitWriterImpl.updateIndex(index)
    else {
      //we create the blob in memory if it doesn't already exists
      SgitWriterImpl.createBlob(filesBlobs(0).contentString.get)

      //we update the index:

      //new blob
      if (index.filter(_.path.equals(filesBlobs(0).path)).isEmpty)
        recAdd(filesBlobs.tail, index :+ filesBlobs(0))

      //blob updated
      else {
        index.map(x =>
          if (x.path.equals(filesBlobs(0).path)) x.hash = filesBlobs(0).hash)
        recAdd(filesBlobs.tail, index)
      }
    }
  }

}