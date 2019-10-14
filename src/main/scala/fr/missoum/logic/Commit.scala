package fr.missoum.logic

import java.time.Instant
case class Commit(var hashParentCommit: String, var hash: String, var treeHash: String, var date: String, var author: String, var message: String) {

  override def toString: String = this.hashParentCommit + " " + this.hash + " " + this.author + "\n" + this.date + " " + this.message + "\n"

  def buildContent: String = "tree " + this.treeHash + "\nparent " + this.hashParentCommit + "\nauthor " + this.author + "\nmessage " + this.message + "\n"
}

object Commit {

  val noParentHash = "0000000000000000000000000000000000000000"

  def apply(hashParentCommit: String, treeHash: String, commitMessage: String): Commit = {
    new Commit(hashParentCommit, "", treeHash, "", "default-user", commitMessage)
  }

  def getCommitFromContent(content: String, hash: String): Commit = {
    val values = content.split("\n").map(x => x.split(" ")).flatten
    new Commit(values(3), hash, values(1), "", "default-user", "")
  }

  def getCommitFromToString(content: String): Commit = {
    val values = content.split("\n").map(x => x.split(" ")).flatten
    new Commit(values(0), values(1), values(2), values(3), values(2), values.drop(4).mkString(" "))
  }

}