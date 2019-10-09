package fr.missoum

import java.io.File.separator

import fr.missoum.utils.helpers.{HashHelper}
import fr.missoum.utils.io.{ConsolePrinter, SgitReader, SgitWriter}

import scala.annotation.tailrec

object CommandExecutor {

  def isCommandForbiddenHere(): Boolean = !SgitReader.isExistingSgitFolder


  def executeInit(): Unit = {
    if (SgitReader.isExistingSgitFolder())
      ConsolePrinter.sgitFolderAlreadyExists()
    else {
      SgitWriter.createSgitRepository()
      ConsolePrinter.sgitFolderCreated()
    }
  }

  @tailrec
  def executeAdd(files: Array[String], linesToAddInIndex: String): Unit = {

    if (files.length == 0) SgitWriter.addToIndex(linesToAddInIndex)

    else {

      val path = System.getProperty("user.dir") + separator + files(0)
      if (SgitReader.fileExists(path)) {
        val content = SgitReader.getContentOfFile(path)
        SgitWriter.createBlob(content)
        var indexLines = linesToAddInIndex
        val line = SgitWriter.buildIndexLine(HashHelper.hashFile(content), path)

        //if not already staged, added to index
        if ((SgitReader.getIndex().find(line.contains).isEmpty)) {
          indexLines += line
          executeAdd(files.tail, (indexLines))
        }
        else
        //if already staged => modified ?
          executeAdd(files.tail, linesToAddInIndex)
      } else {
        ConsolePrinter.fileNotExist(files(0))
        executeAdd(files.tail, linesToAddInIndex)

      }

    }
    //TODO for regexp and file doesn't exists
  }

  def executeGetAllBranchesAndTags() = {
    val currentBranch = SgitReader.getCurrentBranch()
    val tags = SgitReader.getAllTags()
    val branches = SgitReader.getAllBranches()
    ConsolePrinter.printBranchesAndTags(currentBranch, tags, branches)
  }


  def executeCreateNewBranch(newBranch: String) = {
    if (SgitReader.isExistingBranch(newBranch))
      ConsolePrinter.branchAlreadyExists(newBranch)
    else {
      SgitWriter.createNewBranch(newBranch)
      ConsolePrinter.branchCreated(newBranch)
    }
  }

  def executeCreateNewTag(newTag: String) = {
    if (SgitReader.isExistingTag(newTag))
      ConsolePrinter.tagAlreadyExists(newTag)
    else {
      SgitWriter.createNewTag(newTag)
      ConsolePrinter.tagCreated(newTag)
    }
  }
}