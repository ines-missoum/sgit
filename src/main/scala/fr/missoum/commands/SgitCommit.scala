package fr.missoum.commands

import fr.missoum.logic.EntryTree

trait SgitCommit extends SgitCommandHelper{

  def getBlobsToCommit(): Array[EntryTree]

  def commit(blobsToCommit: Array[EntryTree], message: String): Int

  def getAllBlobsCommitted(): Array[EntryTree]

  def createAllTrees(listOfBlobsToCommit: Array[EntryTree]): EntryTree

}