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
  var listEntryTree : Option[List[EntryTree]]

  //functions
  def isTree(): Boolean = entryType.equals(Tree)

  def isBlob(): Boolean = entryType.equals(Blob)

  override def toString: String = this.entryType + " " + this.hash + " " + this.path

}







