package fr.missoum.commands

trait SgitAdd {

  def isNotInWorkspace(fileName: String): Boolean

  def isNotExistingFile(fileName: String): Boolean

  def getNotExistingFile(filesNames: Array[String]): Array[String]

  def addAll(filesNames: Array[String]): Unit


}