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
  def getCommitTag(tag: String): String = readFirstLineFile(PathHelper.TagsDirectory + File.separator + tag)

  /**
   *
   * @param branch
   * @return The hash of the last commit of the branch in parameter
   */
  def getLastCommitOfBranch(branch: String): String = readFirstLineFile(PathHelper.BranchesDirectory + File.separator + branch)

  /**
   *
   * @param hashCommit hash of the commit searched
   * @return true if the commit exists otherwise false
   */
  def isExistingCommit(hashCommit: String): Boolean = {
    val path = PathHelper.ObjectDirectory + File.separator + hashCommit.substring(0, 2) + File.separator + hashCommit.substring(2)
    var commitExists = scala.reflect.io.File(path).exists
    if (commitExists) {
      commitExists = Commit.isContentOfCommit(getContentOfFile(path))
    }
    commitExists
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
  def getCurrentBranch: String = readFirstLineFile(PathHelper.HeadFile)

  /**
   * Retrieves all branches names
   *
   * @return an List that contains all branches names
   */
  def getAllBranches: List[String] = new File(PathHelper.BranchesDirectory).listFiles.map(_.getName).toList


  /**
   * Retrieves all tags names
   *
   * @return an List that contains all tags names
   */
  def getAllTags: List[String] = new File(PathHelper.TagsDirectory).listFiles.map(_.getName).toList

  /**
   *
   * @return all the blobs of the index
   */
  def getIndex: List[EntryTree] = {
    val source = Source.fromFile(PathHelper.IndexFile)
    val index = source.getLines.toList.map(x => Blob(x))
    source.close()
    index
  }

  /**
   * Check if the already is a commit for the current branch.
   *
   * @return True if a commit exists, otherwise False.
   */
  def isExistingCommitOnCurrentBranch: Boolean = {
    val commits = getContentOfFile(PathHelper.BranchesDirectory + File.separator + getCurrentBranch)
    !commits.isEmpty
  }

  /**
   * Reads the content of a file
   *
   * @param path absolute path of the file
   * @return the content of the file
   */
  def getContentOfFile(path: String): String = {
    val source = Source.fromFile(path)
    val content = source.getLines.mkString("\n")
    source.close()
    content
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
  def getContentOfObjectInString(hash: String): String = {
    val pathObject = PathHelper.ObjectDirectory + File.separator + hash.substring(0, 2) + File.separator + hash.substring(2)
    getContentOfFile(pathObject)
  }

  /**
   * Retrieve the last commit
   *
   * @return the last commit done
   */
  def getLastCommitOfCurrentBranch: Commit = {
    val hashLastCommit = getLastCommitHash
    getCommit(hashLastCommit)
  }

  /**
   * Retrieves the commit that corresponds to a hash
   *
   * @param hashCommit hash of a commit
   * @return the commit that corresponds to the hash in parameter
   */
  def getCommit(hashCommit: String): Commit = {
    val pathCommit = PathHelper.ObjectDirectory + File.separator + hashCommit.substring(0, 2) + File.separator + hashCommit.substring(2)
    Commit.getCommitFromContent(getContentOfFile(pathCommit), hashCommit)
  }

  /**
   * Retrieve the logs of the current branch
   *
   * @return the logs of the current branch
   */
  def getLog(): String =
    getContentOfFile(PathHelper.LogsDirectory + File.separator + this.getCurrentBranch)


  /**
   *
   * @return the hash of the last commit
   */
  def getLastCommitHash: String = readFirstLineFile(PathHelper.BranchesDirectory + File.separator + getCurrentBranch)

  private def readFirstLineFile(path: String) = {
    val src = Source.fromFile(path)
    val line = src.getLines.toList.head
    src.close
    line
  }


}
