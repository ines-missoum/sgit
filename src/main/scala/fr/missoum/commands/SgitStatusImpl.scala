package fr.missoum.commands

import fr.missoum.logic.EntryTree

object SgitStatusImpl extends SgitStatus {

  /**
   * By comparing the index and commits, it returns all the blobs to be committed divided in three categories : new files, modified files and deleted files
   *
   * @param index      blobs of the index
   * @param lastCommit blobs that have been committed in the previous commit in their last version
   * @return three lists (each one for a category), in the order : new files, modified files and deleted files
   */
  def getChangesToBeCommitted(index: List[EntryTree], lastCommit: List[EntryTree]): Option[(List[EntryTree], List[EntryTree], List[EntryTree])] = {
    val news = inFirstListButNotInSecondByPath(index, lastCommit)
    val modified = getModifiedElements(index, lastCommit)
    val deleted = inFirstListButNotInSecondByPath(lastCommit, index)
    if (news.isEmpty && modified.isEmpty && deleted.isEmpty)
      None
    else Some((news, modified, deleted))
  }

  /**
   * By comparing the index and workspace, it returns all the blobs not stage for commit divided in two categories : modified files and deleted files
   *
   * @param index     blobs of the index
   * @param workspace blobs of the workspace
   * @return two lists (each one for a category), in the order : modified files and deleted files
   */
  def getChangesNotStagedForCommit(index: List[EntryTree], workspace: List[EntryTree]): Option[(List[EntryTree], List[EntryTree])] = {
    val modified = getModifiedElements(index, workspace)
    val deleted = inFirstListButNotInSecondByPath(index, workspace)
    if (modified.isEmpty && deleted.isEmpty)
      None
    else Some((modified, deleted))

  }

  /**
   * By comparing the index and workspace, it returns all the blobs not tracked
   *
   * @param workspace blobs of the workspace
   * @param index     blobs of the index
   * @return the list of blobs untracked
   */
  def getUntrackedFiles(workspace: List[EntryTree], index: List[EntryTree]): Option[List[EntryTree]] = {
    val untrackedFiles = inFirstListButNotInSecondByPath(workspace, index)
    if (untrackedFiles.isEmpty)
      None
    else
      Some(untrackedFiles)
  }
}