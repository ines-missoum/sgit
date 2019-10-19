package fr.missoum.utils.io.readers

import java.io.File

import fr.missoum.logic.{Blob, Commit, EntryTree}
import fr.missoum.utils.helpers.PathHelper

import scala.io.Source

/**
 * This object is responsible for accessing (read only) files or directories and doing test on it.
 * ie: this object retrieves information from .sgit.
 */
object SgitReaderImpl extends SgitReader {
  /**
   *
   * @param tag the tag
   * @return The commit hash of the tag
   */
  def getCommitTag(tag: String): Option[String] = readFirstLineFile(PathHelper.TagsDirectory + File.separator + tag)

  /**
   *
   * @param branch : the branch
   * @return The hash of the last commit of the branch in parameter
   */
  def getLastCommitOfBranch(branch: String): Option[String] = readFirstLineFile(PathHelper.BranchesDirectory + File.separator + branch)

  /**
   *
   * @param hashCommit hash of the commit searched
   * @return true if the commit exists otherwise false
   */
  def isExistingCommit(hashCommit: String): Boolean = {
    val path = PathHelper.ObjectDirectory + File.separator + hashCommit.substring(0, 2) + File.separator + hashCommit.substring(2)
    val content = getContentOfFile(path)
    if (content.nonEmpty) {
      Commit.isContentOfCommit(content.get)
    } else false
  }

  /**
   * Checks if a .sgit folder already exists in the current directory.
   *
   * @return True if a .sgit folder exists in the current directory, otherwise False.
   */
  def isExistingSgitFolder: Boolean = !PathHelper.SgitPath.equals("")

  /**
   * Checks if a branch exists.
   *
   * @param branch : searched branch
   * @return True if the branch exists, otherwise False.
   */
  def isExistingBranch(branch: String): Boolean = new File(PathHelper.BranchesDirectory + File.separator + branch).isFile

  /**
   * Checks if a tag exists.
   *
   * @param tag : searched tag
   * @return True if the tag exists, otherwise False.
   */
  def isExistingTag(tag: String): Boolean = new File(PathHelper.TagsDirectory + File.separator + tag).isFile

  /**
   * Retrieves the current branch
   *
   * @return the current branch or None if an error happens
   */
  def getCurrentBranch: Option[String] = {
    val line = readFirstLineFile(PathHelper.HeadFile)
    if (line.nonEmpty) Some(line.get.split(" ")(1))
    else None
  }

  /**
   * Retrieves the current branch
   *
   * @return the current branch or None if an error happens
   */
  def getCurrentHead: Option[String] = readFirstLineFile(PathHelper.HeadFile)

  /**
   * Retrieves all branches names
   *
   * @return an List that contains all branches names
   */
  def getAllBranches: Option[List[String]] = getAllRef(PathHelper.BranchesDirectory)

  private def getAllRef(path: String): Option[List[String]] = {
    if (scala.reflect.io.File(path).exists)
      Some(new File(path).listFiles.map(_.getName).toList)
    else None
  }

  /**
   * Retrieves all tags names
   *
   * @return an List that contains all tags names
   */
  def getAllTags: Option[List[String]] = getAllRef(PathHelper.TagsDirectory)

  /**
   *
   * @return all the blobs of the index
   */
  def getIndex: Option[List[EntryTree]] = {
    if (scala.reflect.io.File(PathHelper.IndexFile).exists) {
      val source = Source.fromFile(PathHelper.IndexFile)
      val index = source.getLines.toList.map(x => Blob(x))
      source.close()
      Some(index)
    } else None
  }

  /**
   * Reads the content of a file
   *
   * @param path absolute path of the file
   * @return the content of the file
   */
  def getContentOfFile(path: String): Option[String] = {
    if (scala.reflect.io.File(path).exists) {
      val source = Source.fromFile(path)
      val content = source.getLines.mkString("\n")
      source.close()
      Some(content)
    }
    else None
  }

  /**
   * Reads all the content of an object save in memory
   *
   * @param hash hash of the object
   * @return all the lines of the content of the object
   */
  def getContentOfObjectInEntries(hash: String): List[EntryTree] = {
    val pathObject = PathHelper.ObjectDirectory + File.separator + hash.substring(0, 2) + File.separator + hash.substring(2)
    val source = Source.fromFile(pathObject)
    val content = source.getLines.map(x => EntryTree(x)).toList
    source.close()
    content
  }

  /**
   *
   * @param hash hash of the object
   * @return the content of the object
   */
  def getContentOfObjectInString(hash: String): Option[String] = {
    val pathObject = PathHelper.ObjectDirectory + File.separator + hash.substring(0, 2) + File.separator + hash.substring(2)
    getContentOfFile(pathObject)
  }

  /**
   * Retrieves the commit that corresponds to a hash
   *
   * @param hashCommit hash of a commit
   * @return the commit that corresponds to the hash in parameter
   */
  def getCommit(hashCommit: String): Option[Commit] = {
    val pathCommit = PathHelper.ObjectDirectory + File.separator + hashCommit.substring(0, 2) + File.separator + hashCommit.substring(2)
    val content = getContentOfFile(pathCommit)
    if (content.nonEmpty)
      Some(Commit.getCommitFromContent(content.get, hashCommit))
    else None
  }

  /**
   * Retrieve the logs of the current branch
   *
   * @return the logs of the current branch
   */
  def getLog(branch: String): Option[String] =
    getContentOfFile(PathHelper.LogsDirectory + File.separator + branch)


  /**
   *
   * @return the hash of the last commit
   */
  def getLastCommitHash: Option[String] = {
    val ref = getCurrentHead
    if (ref.nonEmpty) {
      //if head is branch
      if (ref.get.startsWith("ref"))
        readFirstLineFile(PathHelper.BranchesDirectory + File.separator + ref.get.split(" ")(1))
      else
      //head is a commit hash
        ref
    } else None
  }

  private def readFirstLineFile(path: String): Option[String] = {
    val src = Source.fromFile(path)
    val line = src.getLines.toList.headOption
    src.close
    line
  }


}
