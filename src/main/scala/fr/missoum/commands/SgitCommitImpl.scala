package fr.missoum.commands

import java.io.File

import fr.missoum.logic.{Blob, Commit, EntryTree, Tree}
import fr.missoum.utils.io.readers.SgitReaderImpl
import fr.missoum.utils.io.writers.SgitWriterImpl

object SgitCommitImpl extends SgitCommit {

  def firstCommit(message: String): Int = {

    val index = SgitReaderImpl.getIndex().map(x => Blob(x))
    val commitTree = createAllTrees(index)
    val currentBranch = SgitReaderImpl.getCurrentBranch()
    SgitWriterImpl.createCommit(Commit.noParentHash, commitTree, currentBranch, message)
    //TODO create commit + everywhere add this commit = branches log ... and ?
    //TODO tests
    //TODO check if pure
    // TODO clean
    //case when nothing to commit
    index.length

  }


  def commit(message: String): Int = {
    val index = SgitReaderImpl.getIndex().map(x => Blob(x))
    val previousCommit = retrievePreviousBlobsCommitted()
    val blobsToCommit = index.filter(x =>( !previousCommit.exists(y => x.path.equals(y.path) && x.hash.equals(y.hash))))
    val commitTree = createAllTrees(blobsToCommit)

    val parentCommit = SgitReaderImpl.getParentCommitOfCurrentBranch
    val currentBranch = SgitReaderImpl.getCurrentBranch
    SgitWriterImpl.createCommit(parentCommit, commitTree, currentBranch, message)
    blobsToCommit.length

  }

  def retrievePreviousBlobsCommitted(): Array[EntryTree] = {
    val treeCommit = Tree()
    treeCommit.hash = SgitReaderImpl.getLastCommitTreeHash()
    val result = retrievePreviousBlobsCommittedRec(Array(treeCommit))
    result
  }

  def retrievePreviousBlobsCommittedRec(entries: Array[EntryTree]): Array[EntryTree] = {
    val blobs = entries.filter(_.isBlob())
    val trees = entries.filter(_.isTree())

    if (trees.isEmpty)
      blobs
    else {
      val entriesOfTree = trees.map(x => SgitReaderImpl.getContentOfObjectInEntries(x.hash)).flatten
      blobs ++ retrievePreviousBlobsCommittedRec(entriesOfTree)

    }
  }

  def recPrint(tree: EntryTree): Unit = {
    println("TREE : " + tree.path)
    val list: List[EntryTree] = tree.listEntryTree.get
    list.map(x => x.toString).foreach(println(_))
    list.filter(_.isTree()).foreach(recPrint(_))
  }

  def createAllTrees(listOfBlobsToCommit: Array[EntryTree]): EntryTree = {
    val gatherBlobs: Map[String, Array[EntryTree]] = this.gatherBlobs(listOfBlobsToCommit)
    val commitTree: EntryTree = Tree() //to change
    val result = createAllTreesRec(gatherBlobs, commitTree)
    commitTree.listEntryTree = Some(result._1)
    commitTree.hash = result._2
    commitTree

  }

  //ok
  def gatherBlobs(listOfBlobsToCommit: Array[EntryTree]): Map[String, Array[EntryTree]] = {
    //we put all the blobs of the same directory together
    //key = path without file name
    listOfBlobsToCommit.groupBy(_.path.split(File.separator).dropRight(1).mkString(File.separator))

  }

  def createAllTreesRec(gatherBlobs: Map[String, Array[EntryTree]], tree: EntryTree): (List[EntryTree], String) = {

    var list: List[EntryTree] = List[EntryTree]()
    //RETRIEVE BLOBS
    //we add all the blobs of the tree in the entries list of the tree
    val blobs = gatherBlobs.view.filterKeys(_.equals(tree.path)).toMap
    list = list ++ blobs.values.flatten

    //RETRIEVE TREES
    //we keep only the groups of blobs that are entries of the tree (pathTree in parameter)
    var treesPaths = gatherBlobs.keySet

    //if commit tree
    if (tree.path.equals("")) {
      treesPaths = treesPaths.groupBy(_.split(File.separator)(0)).keySet.filter(!_.equals(""))
      treesPaths.map(x => list = list :+ Tree("", x))
    }
    else {
      val entriesFiltered = gatherBlobs.view.filterKeys(_.startsWith(tree.path + File.separator)).toMap
      treesPaths = entriesFiltered.keySet.groupBy(_.toSeq.diff(tree.path.toSeq).unwrap.split(File.separator)(1)).keySet
      treesPaths.map(x => list = list :+ Tree("", tree.path + File.separator + x))
    }

    //SET THE ENTRIES FOR THE ENTRIES TREES
    val treesRec = list.filter(_.isTree())

    treesRec.map(x => {
      val result = createAllTreesRec(gatherBlobs, x)
      x.listEntryTree = Some(result._1)
      x.hash = result._2
    })

    (list, SgitWriterImpl.createObject(list.map(_.toString()).mkString("\n")))
  }

  def retrieveAllCommittedBlobs(): Unit = {

  }
}