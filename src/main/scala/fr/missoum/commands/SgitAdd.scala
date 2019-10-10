package fr.missoum.commands

import java.io.File

import fr.missoum.utils.EntreeTree
import fr.missoum.utils.helpers.{HashHelper, PathHelper}
import fr.missoum.utils.io.{ConsolePrinter, SgitReader, SgitWriter, WorkspaceReader}

import scala.annotation.tailrec

object SgitAdd {

  def getNotExistingFile(filesNames: Array[String]) =
    filesNames.filter(x => !WorkspaceReader.fileExists(System.getProperty("user.dir") + File.separator + x))

  def addAll(filesNames: Array[String]): Unit = {

    // we retrieve all the content of the index file in format of array of blobs
    val indexBlobs = SgitReader.getIndex().map(x => EntreeTree(x))

    //we create a array of blobs from the array of files names
    val newFilesBlobs: Array[EntreeTree] = filesNames.map(x => {
      val absolutePath = System.getProperty("user.dir") + File.separator + x
      val simplePath = PathHelper.getSimplePathOfFile(absolutePath)
      val content = SgitReader.getContentOfFile(absolutePath)
      EntreeTree.NewBlobWithContent(content, simplePath)
    })

    //we add all files that need to
    recAdd(newFilesBlobs, indexBlobs)

  }

  @tailrec
  def recAdd(filesBlobs: Array[EntreeTree], index: Array[EntreeTree]): Unit = {
    //if no more files to deal with, we save the index
    if (filesBlobs.length == 0) SgitWriter.updateIndex(index)
    else {
      //we create the blob in memory if it doesn't already exists
      SgitWriter.createBlob(filesBlobs(0).content.get)

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

  /*@tailrec
  def reccccAdd(files: Array[String], linesToAddInIndex: String): Unit = {

    if (files.length == 0) SgitWriter.addToIndex(linesToAddInIndex)

    else {

      val content = SgitReader.getContentOfFile(path)
      SgitWriter.createBlob(content)
      var indexLines = linesToAddInIndex
      val line = SgitWriter.buildIndexLine(HashHelper.hashFile(content), path)

      //if not already staged, added to index
      if ((SgitReader.getIndex().find(line.contains).isEmpty)) {
        indexLines += line
        recAdd(files.tail, (indexLines))
      }
      else
      //if already staged => modified ?
        recAdd(files.tail, linesToAddInIndex)

    }
  }*/
}