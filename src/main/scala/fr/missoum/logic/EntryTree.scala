package fr.missoum.logic

trait EntryTree {

  //attributes
  val entryType: String
  var path: String
  var hash: String
  var contentString: Option[String]
  var listEntryTree: Option[List[EntryTree]]

  //functions
  def isTree(): Boolean = entryType.equals(EntryTree.TreeType)

  def isBlob(): Boolean = entryType.equals(EntryTree.BlobType)

  override def toString: String = this.entryType + " " + this.hash + " " + this.path

}

object EntryTree {

  //const
  val BlobType = "blob"
  val TreeType = "tree"

  def apply(line: String): EntryTree = {
    val values = line.split(" ")
    if (values(0).equals(TreeType))
      Tree(values(1), values(2))
    else
      Blob(values(1), values(2))
  }

}







