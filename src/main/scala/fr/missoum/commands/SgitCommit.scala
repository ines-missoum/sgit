package fr.missoum.commands

import fr.missoum.logic.EntryTree

trait SgitCommit {

  def getBlobsToCommit(): Array[EntryTree]

  def commit(blobsToCommit: Array[EntryTree], message: String): Int

  def createAllTrees(listOfBlobsToCommit: Array[EntryTree]):EntryTree

  def retrieveAllCommittedBlobs()

}