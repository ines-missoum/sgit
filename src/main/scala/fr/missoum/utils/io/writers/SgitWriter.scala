package fr.missoum.utils.io.writers

import fr.missoum.logic.{Commit, EntryTree}

/**
 * This trait gives the role of accessor of files or directories of sgit repository (in writing).
 */
trait SgitWriter {

  /**
   * Creates the sgit directory where the command has been executed. It contains all the folders and files necessary.
   * It also creates the master branch and checkout on the master branch.
   */
  def createSgitRepository()

  /**
   * Deletes everything in the HEAD file located on top of the .sgit repository and add a line with the name of the branch in parameter or the commit hash.
   * To use this function be sure that the .sgit repository and the parameter branch exist.
   * @param isBranch     indicates if it's a branch
   * @param element : the branch to checkout or a commit hash
   */
  def setHead(element: String, isBranch: Boolean): Unit


  /**
   * Creates the parameter branch.
   * Which means creates all the necessary files for this branch (two files named with the branch name in .sgit/refs/heads/ and .sgit/logs/refs/heads/)
   * To use this function be sure that the .sgit repository exists.
   *
   * @param newBranch      : the branch to create
   * @param hashLastCommit : the last commit hash
   */
  def createNewBranch(newBranch: String, hashLastCommit: String): Unit

  /**
   * Creates the parameter tag.
   * Which means creates all the necessary files for this branch (one file named with the tag name in .sgit/refs/tags/)
   * To use this function be sure that the .sgit repository exists.
   *
   * @param newTag         : the tag to create
   * @param hashLastCommit : the last commit hash
   */
  def createNewTag(newTag: String, hashLastCommit: String): Unit

  /**
   * Creates the blob in the .sgit repository if it doesn't already exists
   *
   * @param contentFile content of the file for which we want to create the blob
   */
  def createObject(contentFile: String): String

  /**
   * Save the index in parameter
   *
   * @param index list of blobs to put in the index
   */
  def updateIndex(index: List[EntryTree])

  /**
   * Create the commit in memory
   *
   * @param commitToSave the commit to save
   * @param branch       the branch or none
   * @return the commit created
   */
  def saveCommit(commitToSave: Commit, branch: Option[String]): Commit

}