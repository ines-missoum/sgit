package fr.missoum.commands

import fr.missoum.logic.EntryTree

trait SgitCommit {

  def firstCommit()

  def commit()

  def createAllTrees(listOfBlobsToCommit: Array[EntryTree])

  def retrieveAllCommittedBlobs()

}