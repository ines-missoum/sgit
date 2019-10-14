package fr.missoum.utils.io.readers

import fr.missoum.logic.{Commit, EntryTree}

trait SgitReader {

  def isExistingSgitFolder(): Boolean

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
  def getCurrentBranch(): String

  /**
   * Retrieves all branches names
   *
   * @return an array that contains all branches names
   */
  def getAllBranches(): Array[String]


  /**
   * Retrieves all tags names
   *
   * @return an array that contains all tags names
   */
  def getAllTags(): Array[String]

  def getIndex(): Array[EntryTree]

  def getContentOfFile(path: String): String

  def isExistingCommit(): Boolean

  def getContentOfObjectInEntries(hash: String): Array[EntryTree]

  def getLastCommit: Commit

  def getCommit(hashCommit: String): Commit

  def getLastCommitHash: String


}