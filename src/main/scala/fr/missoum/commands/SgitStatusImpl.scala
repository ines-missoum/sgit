package fr.missoum.commands

import fr.missoum.logic.EntryTree

object SgitStatusImpl extends SgitStatus {

  private def inFirstListButNotInSecond(l1: Array[EntryTree], l2: Array[EntryTree]): Array[EntryTree] = {
    l1.filter(x => !l2.exists(y => x.path.equals(y.path) && x.hash.equals(y.hash)))
  }

  private def getModifiedElements(l1: Array[EntryTree], l2: Array[EntryTree]): Array[EntryTree] = {
    l1.filter(x => l2.exists(y => x.path.equals(y.path) && !x.hash.equals(y.hash)))
  }

  def getChangesToBeCommitted(): (Array[String], Array[String], Array[String]) = ???

  def getChangesNotStagedForCommit(): (Array[String],Array[String]) = ???

  def getUntrackedFiles(workspace:Array[EntryTree],index:Array[EntryTree]): (Array[String]) = {
    inFirstListButNotInSecond(workspace,index).map(_.path)
  }
}