package fr.missoum.utils
import java.io._
import scala.io.Source

/**
 * This object is responsible for accessing (read only) files or directories and doing test on it.
 * ie: this object retrieves information from .sgit.
 */
object SgitReader{

  /**
   * Checks if a .sgit folder already exists in the current directory.
   * @return True if a .sgit folder exists in the current directory, otherwise False.
   */
  def isExistingSgitFolder() : Boolean = (new File(".sgit")).isDirectory()

  /**
   * Checks if a branch exists.
   * @param branch : searched branch
   * @return True if the branch exists, otherwise False.
   */
  def isExistingBranch(branch:String) : Boolean = (new File(".sgit/refs/heads/"+branch)).isFile()

  /**
   * Checks if a tag exists.
   * @param tag : searched tag
   * @return True if the tag exists, otherwise False.
   */
  def isExistingTag(tag:String) : Boolean = (new File(".sgit/refs/tags/"+tag)).isFile()

  /**
   * Retrieves the current branch
   * @return the current branch or None if an error happens
   */
  def getCurrentBranch() : String = {
    val src = Source.fromFile(".sgit/HEAD")
    val line = src.getLines.toList.head
    src.close
    line
  }

  /**
   * Retrieves all branches names
   * @return an array that contains all branches names
   */
  def getAllBranches():Array[String] = (new File(".sgit/refs/heads")).listFiles.map(_.getName)


  /**
   * Retrieves all tags names
   * @return an array that contains all tags names
   */
  def getAllTags():Array[String] = (new File(".sgit/refs/tags")).listFiles.map(_.getName)
}