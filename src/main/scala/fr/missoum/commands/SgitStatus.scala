package fr.missoum.commands

import fr.missoum.logic.EntryTree

trait SgitStatus extends SgitCommandHelper{

  /**
   * By comparing the index and commits, it returns all the blobs to be committed divided in three categories : new files, modified files and deleted files
   *
   * @param index blobs of the index
   * @param lastCommit blobs that have been committed in the previous commit in their last version
   * @return three lists (each one for a category), in the order : new files, modified files and deleted files
   */
  def getChangesToBeCommitted(index: List[EntryTree], lastCommit: List[EntryTree]): Option[(List[EntryTree], List[EntryTree], List[EntryTree])]

  /**
   * By comparing the index and workspace, it returns all the blobs not stage for commit divided in two categories : modified files and deleted files
   * @param index blobs of the index
   * @param workspace blobs of the workspace
   * @return two lists (each one for a category), in the order : modified files and deleted files
   */
  def getChangesNotStagedForCommit(index: List[EntryTree], workspace: List[EntryTree]): Option[(List[EntryTree], List[EntryTree])]

  /**
   * By comparing the index and workspace, it returns all the blobs not tracked
   * @param workspace blobs of the workspace
   * @param index blobs of the index
   * @return the list of blobs untracked
   */
  def getUntrackedFiles(workspace: List[EntryTree], index: List[EntryTree]): Option[List[EntryTree]]

}