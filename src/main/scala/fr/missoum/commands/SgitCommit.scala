package fr.missoum.commands

import fr.missoum.logic.EntryTree

trait SgitCommit {

  def firstCommit(message:String)

  def commit(message:String)

  def createAllTrees(listOfBlobsToCommit: Array[EntryTree]):EntryTree

  def retrieveAllCommittedBlobs()

}