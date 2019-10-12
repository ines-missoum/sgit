package fr.missoum.logic

import fr.missoum.utils.helpers.HashHelper

case class Blob(val entryType: String, var hash: String, var path: String, var contentString: Option[String],var listEntryTree : Option[List[EntryTree]] ) extends EntryTree{
  def addContent(content:String):Blob ={
    val blobWithContentUpdated:Blob= this.copy()
    blobWithContentUpdated.contentString = Some(content)
    blobWithContentUpdated.hash = HashHelper.hashFile(content)
    blobWithContentUpdated
  }
}

object Blob{

  def apply(line: String): EntryTree = {
    val values = line.split(" ")
    new Blob(EntryTree.BlobType, values(1), values(2), None, None)
  }

  def apply(hash: String, path: String): EntryTree = new Blob(EntryTree.BlobType, hash, path, None, None)

  def newBlobWithContent(content: String, path: String): EntryTree = new Blob(EntryTree.BlobType, HashHelper.hashFile(content), path, Some(content), None)

}
