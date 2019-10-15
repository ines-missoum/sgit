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
  def getChangesToBeCommitted(index: List[EntryTree], lastCommit: List[EntryTree]): Option[(List[String], List[String], List[String])] = {
    val news = inFirstListButNotInSecond(index, lastCommit).map(_.path)
    val modified = getModifiedElements(index, lastCommit).map(_.path)
    val deleted = inFirstListButNotInSecond(lastCommit, index).map(_.path)
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
  def getChangesNotStagedForCommit(index: List[EntryTree], workspace: List[EntryTree]): Option[(List[String], List[String])] = {
    val modified = getModifiedElements(index, workspace).map(_.path)
    val deleted = inFirstListButNotInSecond(index, workspace).map(_.path)
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
  def getUntrackedFiles(workspace: List[EntryTree], index: List[EntryTree]): Option[List[String]] = {
    val untrackedFiles = inFirstListButNotInSecond(workspace, index).map(_.path)
    if (untrackedFiles.isEmpty)
      None
    else
      Some(untrackedFiles)
  }
}