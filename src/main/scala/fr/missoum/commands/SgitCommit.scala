package fr.missoum.commands

import fr.missoum.logic.{Commit, EntryTree}
import fr.missoum.utils.io.readers.SgitReader
import fr.missoum.utils.io.writers.SgitWriter

trait SgitCommit extends SgitCommandHelper {
  /**
   * Responsible for reading in memory
   */
  var sgitReader: SgitReader

  /**
   * Responsible for writing in memory
   */
  var sgitWriter: SgitWriter

  /**
   *
   * @param index the index blobs
   * @param blobsOfLastCommit the blobs of the last commit
   * @return the number of changes (creations + modifications)
   */
  def nbFilesChangedSinceLastCommit(index: List[EntryTree], blobsOfLastCommit: List[EntryTree]): Option[Int]
  /*/**
   * Gives the list of all the blobs to commit (ie: blobs that have been created or modified since the previous commits)
   *
   * @param isFirstCommit Indicates if it's the first commit or not
   * @return The list of all the blobs to commit
   */
  def getBlobsToCommit(isFirstCommit: Boolean): List[EntryTree]*/

  /**
   * Creates the commit object and creates the trees in memory
   *
   * @param lastCommitHash None if it's a first commit else the hash of the last commit
   * @param blobsToCommit List of blobs to add to the commit
   * @param message       Message of the commit
   * @return
   */
  def commit(lastCommitHash: Option[String], blobsToCommit: List[EntryTree], message: String): Commit

  /**
   *
   * @param lastCommit None if it's a first commit else the last commit
   * @return the list of the blobs of the last commit
   */
  def getBlobsLastCommit(lastCommit: Option[Commit]): List[EntryTree]
 /* /**
   * Retrieve all the last version of all blobs committed in the previous commits
   * @return The list of blobs committed in the previous commits in their last version
   */
  def getAllBlobsCommitted(isFirstCommit: Boolean): List[EntryTree]*/

  /**
   * From the list of blobs to commit, it constructs all the trees and blobs and link them together
   *
   * @param listOfBlobsToCommit list of blobs to commit
   * @return The commit tree well constructed with all its trees and blobs
   */
  def createAllTrees(listOfBlobsToCommit: List[EntryTree]): EntryTree

  /**
   *
   * @param commit : commit object
   * @return The list of blobs of the commit
   */
  def getBlobsOfCommit(commit: Commit): List[EntryTree]

}