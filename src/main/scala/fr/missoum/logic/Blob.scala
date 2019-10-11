package fr.missoum.logic

import fr.missoum.utils.helpers.HashHelper

case class Blob(val entryType: String, var hash: String, var path: String, var contentString: Option[String]) extends EntryTree

object Blob{

  def apply(line: String): EntryTree = {
    val values = line.split(" ")
    new Blob("blob", values(1), values(2), None)
  }

  def apply(hash: String, path: String): EntryTree = new Blob("blob", hash, path, None)

  def NewBlobWithContent(content: String, path: String): EntryTree = new Blob("blob", HashHelper.hashFile(content), path, Some(content))

}
