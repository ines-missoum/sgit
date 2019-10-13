package fr.missoum.commands

import java.io.File

import fr.missoum.logic.{Blob, Commit, EntryTree, Tree}
import fr.missoum.utils.io.readers.SgitReaderImpl
import fr.missoum.utils.io.writers.SgitWriterImpl

import scala.annotation.tailrec

object SgitCommitImpl extends SgitCommit {

  /**
   *
   * @param blobsToCommit
   * @param message
   * @return the nb of files modified from last commit(if exists)
   */
  def commit(blobsToCommit: Array[EntryTree], message: String): Int = {
    var parentCommit = ""

    //if it's the first commit then it has no parent else we retrieve it's parent commit
    if (!SgitReaderImpl.isExistingCommit()) parentCommit = Commit.noParentHash
    else parentCommit = SgitReaderImpl.getLastCommitHash

    //we create the commit and save it
    val commitTree = createAllTrees(blobsToCommit)
    val currentBranch = SgitReaderImpl.getCurrentBranch
    SgitWriterImpl.createCommit(parentCommit, commitTree, currentBranch, message)
    blobsToCommit.length
  }

  def getBlobsToCommit(): Array[EntryTree] = {

    val index = SgitReaderImpl.getIndex().map(x => Blob(x))
    //if first commit we commit all the content of the index
    if (!SgitReaderImpl.isExistingCommit()) index
    //else we commit the differences between the index and the last commit
    else {
      val previousCommit = getAllBlobsCommitted()
      val blobsToCommit = index.filter(x => (!previousCommit.exists(y => x.path.equals(y.path) && x.hash.equals(y.hash))))
      blobsToCommit
    }
  }

  def getAllBlobsCommitted(): Array[EntryTree] = {

    if (!SgitReaderImpl.isExistingCommit()) Array[EntryTree]()
    else
      getAllBlobsCommittedRec(SgitReaderImpl.getLastCommit, Array[EntryTree]())
  }

  @tailrec
  def getAllBlobsCommittedRec(commit: Commit, result: Array[EntryTree]): Array[EntryTree] = {
    // we retrieve the blobs of the commit
    val treeCommit = Tree()
    treeCommit.hash = commit.treeHash
    val blobsOfCommit = getBlobsOfACommitRec(Array(treeCommit))
    val newResult = result ++ inFirstListButNotInSecond(blobsOfCommit, result)

    // end if it's the first commit
    if (commit.hashParentCommit.equals(Commit.noParentHash))
      newResult
    else {
      val nexCommit = SgitReaderImpl.getCommit(commit.hashParentCommit)
      //we want only the last version of each blobs
      getAllBlobsCommittedRec(nexCommit, newResult)
    }

  }

  def getBlobsOfACommitRec(entries: Array[EntryTree]): Array[EntryTree] = {
    val blobs = entries.filter(_.isBlob())
    val trees = entries.filter(_.isTree())

    if (trees.isEmpty) blobs
    else {
      val entriesOfTree = trees.map(x => SgitReaderImpl.getContentOfObjectInEntries(x.hash)).flatten
      blobs ++ getBlobsOfACommitRec(entriesOfTree)
    }
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

}