package fr.missoum.commands

import fr.missoum.logic.EntryTree

object SgitStatusImpl extends SgitStatus {

  private def inFirstListButNotInSecond(l1: Array[EntryTree], l2: Array[EntryTree]): Array[EntryTree] = {
    l1.filter(x => !l2.exists(y => x.path.equals(y.path)))
  }

  private def getModifiedElements(l1: Array[EntryTree], l2: Array[EntryTree]): Array[EntryTree] = {
    l1.filter(x => l2.exists(y => x.path.equals(y.path) && !x.hash.equals(y.hash)))
  }

  def getChangesToBeCommitted(index: Array[EntryTree], lastCommit: Array[EntryTree]): (Array[String], Array[String], Array[String]) = {
    val news = inFirstListButNotInSecond(index, lastCommit).map(_.path)
    val modified = getModifiedElements(index, lastCommit).map(_.path)
    val deleted = inFirstListButNotInSecond(lastCommit, index).map(_.path)
    (news, modified, deleted)
  }

  def getChangesNotStagedForCommit(index: Array[EntryTree], workspace: Array[EntryTree]): (Array[String], Array[String]) = {
    val modified = getModifiedElements(index, workspace).map(_.path)
    val deleted = inFirstListButNotInSecond(index, workspace).map(_.path)
    (modified, deleted)
  }

  def getUntrackedFiles(workspace: Array[EntryTree], index: Array[EntryTree]): Array[String] = {
    inFirstListButNotInSecond(workspace, index).map(_.path)
  }
}