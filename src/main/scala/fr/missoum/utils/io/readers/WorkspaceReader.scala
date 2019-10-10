package fr.missoum.utils.io.readers

trait WorkspaceReader{

  def fileExists(path: String): Boolean
}