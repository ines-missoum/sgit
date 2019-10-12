package fr.missoum.commands

import fr.missoum.logic.EntryTree

trait SgitCommit {

  def firstCommit(message:String):Int

  def commit(message:String):Int

  def createAllTrees(listOfBlobsToCommit: Array[EntryTree]):EntryTree

  def retrieveAllCommittedBlobs()

}