package fr.missoum.utils.io.readers

import fr.missoum.logic.{Commit, EntryTree}

/**
 * This trait gives the role of accessor of files or directories of sgit repository (in reading only).
 */
trait SgitReader {
  /**
   *
   * @param hash hash of the object
   * @return the content of the object
   */
  def getContentOfObjectInString(hash: String): String

  /**
   *
   * @param tag the tag
   * @return The commit hash of the tag
   */
  def getCommitTag(tag: String): Option[String]

  /**
   *
   * @param branch the branch
   * @return The hash of the last commit of the branch in parameter
   */
  def getLastCommitOfBranch(branch: String): Option[String]

  /**
   * Searches if this commit exists somewhere
   *
   * @param hashCommit hash of the commit searched
   * @return true if the commit exists otherwise false
   */
  def isExistingCommit(hashCommit: String): Boolean


  /**
   * Checks if a .sgit folder already exists in the current directory.
   *
   * @return True if a .sgit folder exists in the current directory, otherwise False.
   */
  def isExistingSgitFolder: Boolean

  /**
   * Checks if a branch exists.
   *
   * @param branch : searched branch
   * @return True if the branch exists, otherwise False.
   */
  def isExistingBranch(branch: String): Boolean

  /**
   * Checks if a tag exists.
   *
   * @param tag : searched tag
   * @return True if the tag exists, otherwise False.
   */
  def isExistingTag(tag: String): Boolean

  /**
   * Retrieves the current branch
   *
   * @return the current branch or None if an error happens
   */
  def getCurrentBranch: Option[String]

  /**
   * Retrieves all branches names
   *
   * @return an List that contains all branches names
   */
  def getAllBranches: List[String]


  /**
   * Retrieves all tags names
   *
   * @return an List that contains all tags names
   */
  def getAllTags: List[String]

  /**
   *
   * @return all the blobs of the index
   */
  def getIndex: List[EntryTree]

  /**
   * Reads the content of a file
   *
   * @param path absolute path of the file
   * @return the content of the file
   */
  def getContentOfFile(path: String): String

  /**
   * Reads all the content of an object save in memory
   *
   * @param hash hash of the object
   * @return all the lines of the content of the object
   */
  def getContentOfObjectInEntries(hash: String): List[EntryTree]

  /**
   * Retrieves the commit that corresponds to a hash
   *
   * @param hashCommit hash of a commit
   * @return the commit that corresponds to the hash in parameter
   */
  def getCommit(hashCommit: String): Commit

  /**
   *
   * @return the hash of the last commit
   */
  def getLastCommitHash: Option[String]

  /**
   * Retrieve the logs of the current branch
   *
   * @return the logs of the current branch
   */
  def getLog(branch: String): String

}