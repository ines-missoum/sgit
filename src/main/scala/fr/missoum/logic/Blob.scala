package fr.missoum.logic

import fr.missoum.utils.helpers.HashHelper

case class Blob(val entryType: String, var hash: String, var path: String, var contentString: Option[String],var listEntryTree : Option[List[EntryTree]] ) extends EntryTree

object Blob{

  def apply(line: String): EntryTree = {
    val values = line.split(" ")
    new Blob(EntryTree.BlobType, values(1), values(2), None, None)
  }

  def apply(hash: String, path: String): EntryTree = new Blob(EntryTree.BlobType, hash, path, None, None)

  def NewBlobWithContent(content: String, path: String): EntryTree = new Blob(EntryTree.BlobType, HashHelper.hashFile(content), path, Some(content), None)

}
