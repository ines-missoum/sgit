package fr.missoum.utils

import fr.missoum.utils.helpers.HashHelper

object EntreeTree {

  val Blob = "blob"
  val Tree = "tree"

  def NewBlobWithContent(content: String, path: String): EntreeTree = {
    new EntreeTree(EntreeTree.Blob, HashHelper.hashFile(content), path, Some(content))
  }
  def NewBlob(hash: String, path: String): EntreeTree = {
    new EntreeTree(EntreeTree.Blob, hash, path, None)
  }

  def NewTree(hash: String, path: String): EntreeTree = {
    new EntreeTree(EntreeTree.Tree, hash, path, None)
  }

  def apply(line: String): EntreeTree = {
    val values = line.split(" ")
    new EntreeTree(values(0), values(1), values(2), None)
  }
}

case class EntreeTree(var entreeType: String, var hash: String, var path: String, var content : Option[String]) {
  override def toString: String = this.entreeType+" "+ this.hash +" "+this.path

  def isTree(): Boolean = this.entreeType.equals(EntreeTree.Tree)

  def isBlob(): Boolean = this.entreeType.equals(EntreeTree.Blob)
}




