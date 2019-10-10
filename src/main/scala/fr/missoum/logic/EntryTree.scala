package fr.missoum.logic

import fr.missoum.utils.helpers.HashHelper

object EntryTree {

  val Blob = "blob"
  val Tree = "tree"

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
}

case class EntryTree(var entreeType: String, var hash: String, var path: String, var content : Option[String]) {
  override def toString: String = this.entreeType+" "+ this.hash +" "+this.path

  def isTree(): Boolean = this.entreeType.equals(EntryTree.Tree)

  def isBlob(): Boolean = this.entreeType.equals(EntryTree.Blob)
}




