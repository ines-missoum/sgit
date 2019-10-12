package fr.missoum.logic

case class Tree(entryType: String, var hash: String, var path: String, var contentString: Option[String],var listEntryTree : Option[List[EntryTree]] ) extends EntryTree

object Tree {

  def apply(line: String): EntryTree = {
    val values = line.split(" ")
    new Tree(EntryTree.TreeType, values(1), values(2), None, None)
  }

  def apply(hash: String, path: String): EntryTree = {
    new Tree(EntryTree.TreeType, hash, path, None, None )
  }

  def apply():EntryTree ={
    new Tree(EntryTree.TreeType, "", "", None, Some(List[EntryTree]()))
  }

}
