package fr.missoum.utils.io

import java.io._

import fr.missoum.utils.helpers.PathHelper

import scala.io.Source

/**
 * This object is responsible for accessing (read only) files or directories and doing test on it.
 * ie: this object retrieves information from .sgit.
 */
object SgitReader {

  /**
   * Checks if a .sgit folder already exists in the current directory.
   *
   * @return True if a .sgit folder exists in the current directory, otherwise False.
   */
  def isExistingSgitFolder(): Boolean = !PathHelper.SgitPath.equals("")

  /**
   * Checks if a branch exists.
   *
   * @param branch : searched branch
   * @return True if the branch exists, otherwise False.
   */
  def isExistingBranch(branch: String): Boolean = (new File(PathHelper.BranchesDirectory + File.separator + branch)).isFile()

  /**
   * Checks if a tag exists.
   *
   * @param tag : searched tag
   * @return True if the tag exists, otherwise False.
   */
  def isExistingTag(tag: String): Boolean = (new File(PathHelper.TagsDirectory + File.separator + tag)).isFile()

  /**
   * Retrieves the current branch
   *
   * @return the current branch or None if an error happens
   */
  def getCurrentBranch(): String = readFirstLineFile(PathHelper.HeadFile)

  /**
   * Retrieves all branches names
   *
   * @return an array that contains all branches names
   */
  def getAllBranches(): Array[String] = (new File(PathHelper.BranchesDirectory)).listFiles.map(_.getName)


  /**
   * Retrieves all tags names
   *
   * @return an array that contains all tags names
   */
  def getAllTags(): Array[String] = (new File(PathHelper.TagsDirectory)).listFiles.map(_.getName)

  def getIndex(): Array[String] = Source.fromFile(PathHelper.IndexFile).getLines().toArray



  def getContentOfFile(path: String): String = Source.fromFile(path).getLines.mkString("\n")

  //private functions
  private def readFirstLineFile(path: String) = {
    val src = Source.fromFile(path)
    val line = src.getLines.toList.head
    src.close
    line
  }

}