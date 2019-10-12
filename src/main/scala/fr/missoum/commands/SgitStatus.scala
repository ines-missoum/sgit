package fr.missoum.commands

import fr.missoum.logic.EntryTree

trait SgitStatus {

  def getChangesNotStagedForCommit(index: Array[EntryTree], workspace: Array[EntryTree]): (Array[String], Array[String])

  def getUntrackedFiles(workspace: Array[EntryTree], index: Array[EntryTree]): (Array[String])

}