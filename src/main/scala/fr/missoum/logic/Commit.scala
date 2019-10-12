package fr.missoum.logic

import fr.missoum.utils.helpers.HashHelper

case class Commit(var HashParentCommit: String, var hash: String, var treeHash: String, var date: String,var author:String, var message:String){
  override def toString: String = this.HashParentCommit + " " + this.hash + " " + this.author+"\n" + this.date + " "+this.message
  def buildContent = "tree "+this.treeHash+"\nauthor "+this.author+"\nmessage "+this.message
}

object Commit {

  val noParentHash = "0000000000000000000000000000000000000000"

  def apply(HashParentCommit: String, treeHash: String,date: String): Commit = {

    new Commit(HashParentCommit, "",treeHash, date, "Default user", "Default message")
  }


}