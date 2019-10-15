package fr.missoum.utils.io.writers

import fr.missoum.logic.{Commit, EntryTree}

/**
 * This trait gives the role of accessor of files or directories of sgit repository (in writing).
 */
trait SgitWriter {

  def createSgitRepository()

  /**
   * Deletes everything in the HEAD file located on top of the .sgit repository and add a line with the name of the branch in parameter.
   * To use this function be sure that the .sgit repository and the parameter branch exist.
   *
   * @param branch : the branch to checkout
   */
  def setHeadBranch(branch: String): Unit


  /**
   * Creates the parameter branch.
   * Which means creates all the necessary files for this branch (two files named with the branch name in .sgit/refs/heads/ and .sgit/logs/refs/heads/)
   * To use this function be sure that the .sgit repository exists.
   *
   * @param newBranch : the branch to create
   */
  def createNewBranch(newBranch: String): Unit

  /**
   * Creates the parameter tag.
   * Which means creates all the necessary files for this branch (one file named with the tag name in .sgit/refs/tags/)
   * To use this function be sure that the .sgit repository exists.
   *
   * @param newTag : the tag to create
   */
  def createNewTag(newTag: String): Unit

  /**
   * Creates the blob in the .sgit repository if it doesn't already exists
   *
   * @param contentFile content of the file for which we want to create the blob
   */
  def createObject(contentFile: String):String

  /**
   * Save the index in parameter
   * @param index list of blobs to put in the index
   */
  def updateIndex(index: List[EntryTree])
  /**
   * Create the commit in memory
   * @param commitToSave the commit to save
   * @param currentBranch the branch where the commit should be saved
   * @return the commit created
   */
  def saveCommit(commitToSave:Commit, currentBranch: String): Commit

}