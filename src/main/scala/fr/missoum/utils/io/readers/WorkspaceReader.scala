package fr.missoum.utils.io.readers

import fr.missoum.logic.EntryTree

trait WorkspaceReader {

  def fileExists(path: String): Boolean

  def getAllBlobsOfWorkspace(): Array[EntryTree]
}