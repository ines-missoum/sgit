package fr.missoum.commands

import java.io.File

import fr.missoum.logic.{Commit, EntryTree, Tree}
import fr.missoum.utils.io.readers.{SgitReader, SgitReaderImpl}
import fr.missoum.utils.io.writers.{SgitWriter, SgitWriterImpl}

object SgitCommitImpl extends SgitCommit {

  /**
   * Responsible for reading in memory
   */
  var sgitReader: SgitReader = SgitReaderImpl
  /**
   * Responsible for writing in memory
   */
  var sgitWriter: SgitWriter = SgitWriterImpl

  var sgitStatus: SgitStatus = SgitStatusImpl

  /**
   * Creates the commit object and creates the trees in memory
   *
   * @param lastCommitHash None if it's a first commit else the hash of the last commit
   * @param currentBranch Branch where the commit needs to be recorded
   * @param blobsToCommit List of blobs to add to the commit
   * @param message       Message of the commit
   * @return
   */
  def commit(lastCommitHash: Option[String], currentBranch: String, blobsToCommit: List[EntryTree], message: String): Commit = {

    // we  retrieve the parent commit
    var parentCommit = ""
    if (lastCommitHash.isEmpty) parentCommit = Commit.noParentHash
    else parentCommit = lastCommitHash.get

    //creation of the commit
    val commitTree = createAllTrees(blobsToCommit)
    Commit(parentCommit, commitTree.hash, message)

  }

  /**
   *
   * @param lastCommit None if it's a first commit else the last commit
   * @return the list of the blobs of the last commit
   */
  def getBlobsLastCommit(lastCommit: Option[Commit]): List[EntryTree] = {

    if (lastCommit.isEmpty) List[EntryTree]()
    else {
      getBlobsOfCommit(lastCommit.get)
    }
  }

  /**
   *
   * @param commit : commit object
   * @return The list of blobs of the commit
   */
  def getBlobsOfCommit(commit: Commit): List[EntryTree] = {
    val treeCommit = Tree()
    treeCommit.hash = commit.treeHash
    getBlobsRec(List(treeCommit))
  }


  /**
   *
   * @param index             the index blobs
   * @param blobsOfLastCommit the blobs of the last commit
   * @return the number of changes (creations + modifications)
   */
  def nbFilesChangedSinceLastCommit(index: List[EntryTree], blobsOfLastCommit: List[EntryTree]): Option[Int] = {
    val statusCommit = sgitStatus.getChangesToBeCommitted(index, blobsOfLastCommit)
    if (statusCommit.isEmpty) {
      None
    } else {
      Some(statusCommit.get._1.length + statusCommit.get._2.length)
    }
  }

  /*
  /**
   * Gives the list of all the blobs to commit (ie: blobs that have been created or modified since the previous commits)
   * @param isFirstCommit Indicates if it's the first commit or not
   * @return The list of all the blobs to commit
   */
    //not used
  def getBlobsToCommit(isFirstCommit: Boolean): List[EntryTree] = {

    val index = sgitReader.getIndex
    //if first commit we commit all the content of the index
    if (isFirstCommit) index
    //else we commit the differences between the index and the last commit
    else {
      val previousCommit = getAllBlobsCommitted(isFirstCommit)
      val blobsToCommit = index.filter(x => !previousCommit.exists(y => x.path.equals(y.path) && x.hash.equals(y.hash)))
      blobsToCommit
    }
  }

  /**
   * Retrieve all the last version of all blobs committed in the previous commits
   * @return The list of blobs committed in the previous commits in their last version
   */
    //not used
  def getAllBlobsCommitted(isFirstCommit: Boolean): List[EntryTree] = {

    if (isFirstCommit) List[EntryTree]()
    else
      getAllBlobsCommittedRec(sgitReader.getLastCommit, List[EntryTree]())
  }

  /**
   * Retrieve all the last version of all blobs committed in the previous commits (since the commit parameter)
   * @param commit Current commit for whom we want to retrieve the committed blobs
   * @param result Current result (tail rec)
   * @return The list of blobs committed in the previous commits in their last version
   */
  @tailrec
  //not used
  private def getAllBlobsCommittedRec(commit: Commit, result: List[EntryTree]): List[EntryTree] = {
    // we retrieve the blobs of the commit
    val treeCommit = Tree()
    treeCommit.hash = commit.treeHash
    val blobsOfCommit = getBlobsRec(List(treeCommit))
    val newResult = result ++ inFirstListButNotInSecond(blobsOfCommit, result)

    // end if it's the first commit
    if (commit.hashParentCommit.equals(Commit.noParentHash))
      newResult
    else {
      val nexCommit = sgitReader.getCommit(commit.hashParentCommit)
      //we want only the last version of each blobs
      getAllBlobsCommittedRec(nexCommit, newResult)
    }

  }
*/
  /**
   * Retrieves all the blobs from the entries
   *
   * @param entries List of entries tree
   * @return The list of blobs of the entries
   */
  private def getBlobsRec(entries: List[EntryTree]): List[EntryTree] = {
    val blobs = entries.filter(_.isBlob())
    val trees = entries.filter(_.isTree())

    if (trees.isEmpty) blobs
    else {
      val entriesOfTree = trees.flatMap(x => sgitReader.getContentOfObjectInEntries(x.hash))
      blobs ++ getBlobsRec(entriesOfTree)
    }
  }

  /**
   * From the list of blobs to commit, it constructs all the trees and blobs and link them together
   *
   * @param listOfBlobsToCommit list of blobs to commit
   * @return The commit tree well constructed with all its trees and blobs
   */
  def createAllTrees(listOfBlobsToCommit: List[EntryTree]): EntryTree = {
    val gatherBlobs: Map[String, List[EntryTree]] = this.gatherBlobs(listOfBlobsToCommit)
    val commitTree: EntryTree = Tree()
    val (commitTreeListEntries, hashCommitTree) = createAllTreesRec(gatherBlobs, commitTree)
    commitTree.listEntryTree = Some(commitTreeListEntries)
    commitTree.hash = hashCommitTree
    commitTree

  }

  /**
   * Puts all the blobs of the same directory together
   *
   * @param blobs Blobs to gathered
   * @return A map that gathered the blobs by path (key = path without file name)
   */
  private def gatherBlobs(blobs: List[EntryTree]): Map[String, List[EntryTree]] =
    blobs.groupBy(_.path.split(File.separator).dropRight(1).mkString(File.separator))

  /**
   * From the gatherBlobs, it constructs recursively all the trees and blobs and link them together
   *
   * @param gatherBlobs A map where the blobs are gathered by path (key = path without file name)
   * @param tree        Starting tree : tree from whom all its trees and blobs will be built and linked
   * @return The hash of the commit tree and its list of entries (blobs and trees)
   */
  private def createAllTreesRec(gatherBlobs: Map[String, List[EntryTree]], tree: EntryTree): (List[EntryTree], String) = {

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
      treesPaths = treesPaths.groupBy(_.split(File.separator)(0))
        .keySet
        .filter(!_.equals(""))
      treesPaths.map(x => list = list :+ Tree("", x))
    }
    else {
      val entriesFiltered = gatherBlobs.view
        .filterKeys(_.startsWith(tree.path + File.separator))
        .toMap
      treesPaths = entriesFiltered.keySet
        .groupBy(_.toSeq.diff(tree.path.toSeq).unwrap.split(File.separator)(1))
        .keySet
      treesPaths.map(x => list = list :+ Tree("", tree.path + File.separator + x))
    }

    //REC : SET THE ENTRIES FOR THE CHILDREN TREES
    val treesRec = list.filter(_.isTree())

    treesRec.map(x => {
      val result = createAllTreesRec(gatherBlobs, x)
      x.listEntryTree = Some(result._1)
      x.hash = result._2
    })

    //CREATION IN MEMORY
    (list, sgitWriter.createObject(list.map(_.toString()).mkString("\n")))
  }

}