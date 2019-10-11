package fr.missoum.logic

trait EntryTree {

  //const
  val Blob = "blob"
  val Tree = "tree"

  //attributes
  val entryType: String
  var path: String
  var hash: String
  var contentString: Option[String]

  //functions
  def isTree(): Boolean = entryType.equals(Tree)

  def isBlob(): Boolean = entryType.equals(Blob)

  override def toString: String = this.entryType + " " + this.hash + " " + this.path

}

/*object EntryTree {

 def NewBlobWithContent(content: String, path: String): EntryTree = {
    new EntryTree(EntryTree.Blob, HashHelper.hashFile(content), path, Some(content))
  }
  def NewBlob(hash: String, path: String): EntryTree = {
    new EntryTree(EntryTree.Blob, hash, path, None)
  }

  def NewTree(hash: String, path: String): EntryTree = {
    new EntryTree(EntryTree.Tree, hash, path, None)
  }

  def apply(line: String): EntryTree = {
    val values = line.split(" ")
    new EntryTree(values(0), values(1), values(2), None)
  }
}*/






