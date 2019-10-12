package fr.missoum.commands

import fr.missoum.logic.EntryTree

trait SgitStatus {

  def getUntrackedFiles(workspace:Array[EntryTree],index:Array[EntryTree]): (Array[String])

}