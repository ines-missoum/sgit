package fr.missoum.logic

case class Tree(val entryType: String, var hash: String, var path: String, var contentString: Option[String],var listEntryTree : Option[List[EntryTree]] ) extends EntryTree

object Tree {

  def apply(line: String): EntryTree = {
    val values = line.split(" ")
    new Tree("tree", values(1), values(2), None, None)
  }

  def apply(hash: String, path: String): EntryTree = {
    new Tree("tree", hash, path, None, None )
  }

}
