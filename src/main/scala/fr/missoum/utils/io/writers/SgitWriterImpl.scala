package fr.missoum.utils.io.writers

import java.io.{BufferedWriter, File, FileWriter}
import java.util.Calendar

import fr.missoum.logic.{Commit, EntryTree}
import fr.missoum.utils.helpers.{HashHelper, PathHelper}

/**
 * This object is responsible for all writing actions in sgit repository. Which means create files and folders and update them.
 */
object SgitWriterImpl extends SgitWriter {

  /**
   * Creates the sgit directory where the command has been executed. It contains all the folders and files necessary.
   * It also creates the master branch and checkout on the master branch.
   */
  def createSgitRepository(): Unit = {

    //creation of all files and folders
    val listFolders = List(PathHelper.BranchesDirectory, PathHelper.TagsDirectory, PathHelper.LogsDirectory, PathHelper.ObjectDirectory)
    val listFiles = List(PathHelper.HeadLogFile, PathHelper.HeadFile, PathHelper.IndexFile)
    listFolders.map(new File(_).mkdirs())
    listFiles.map(new File(_).createNewFile)

    //creation of the master branch and checkout
    new File(PathHelper.BranchesDirectory + File.separator + "master").createNewFile
    new File(PathHelper.LogsDirectory + File.separator + "master").createNewFile
    setHead("master",true)

  }

  /**
   * Deletes everything in the HEAD file located on top of the .sgit repository and add a line with the name of the branch in parameter or the commit hash.
   * To use this function be sure that the .sgit repository and the parameter branch exist.
   *
   * @param element : the branch to checkout or a commit hash
   */
  def setHead(element: String, isBranch: Boolean): Unit = {
    if (isBranch)
      writeInFile(PathHelper.HeadFile, "ref " + element, shouldBeAppend = false)
    else
      writeInFile(PathHelper.HeadFile, element, shouldBeAppend = false)
  }


  /**
   * Creates the parameter branch.
   * Which means creates all the necessary files for this branch (two files named with the branch name in .sgit/refs/heads/ and .sgit/logs/refs/heads/)
   * To use this function be sure that the .sgit repository exists.
   *
   * @param newBranch  : the branch to create
   * @param hashLastCommit : the last commit hash
   */
  def createNewBranch(newBranch: String, hashLastCommit: String): Unit = {
    new File(PathHelper.BranchesDirectory + File.separator + newBranch).createNewFile
    new File(PathHelper.LogsDirectory + File.separator + newBranch).createNewFile
    writeInFile(PathHelper.BranchesDirectory + File.separator + newBranch, hashLastCommit, shouldBeAppend = false)
  }

  /**
   * Creates the parameter tag.
   * Which means creates all the necessary files for this branch (one file named with the tag name in .sgit/refs/tags/)
   * To use this function be sure that the .sgit repository exists.
   *
   * @param newTag     : the tag to create
   * @param hashLastCommit : the last commit hash
   */
  def createNewTag(newTag: String, hashLastCommit: String): Unit = {
    new File(PathHelper.TagsDirectory + File.separator + newTag).createNewFile
    writeInFile(PathHelper.TagsDirectory + File.separator + newTag, hashLastCommit, shouldBeAppend = false)
  }


  /**
   * Creates the object in the .sgit repository if it doesn't already exists
   *
   * @param contentFile content of the file for which we want to create the blob
   * @return the hash of the object created
   */
  def createObject(contentFile: String): String = {

    //retrieves path from hash of content file
    val hash = HashHelper.hashFile(contentFile)
    val pathFolder = PathHelper.ObjectDirectory + File.separator + hash.substring(0, 2)
    val pathFile = pathFolder + File.separator + hash.substring(2)

    //creation of folder and file if do not exist
    new File(pathFolder).mkdir()
    val isNew = new File(pathFile).createNewFile
    if (isNew) {
      writeInFile(pathFile, contentFile, shouldBeAppend = false)
    }
    hash
  }

  /**
   * Save the index in parameter
   *
   * @param index list of blobs to put in the index
   */
  def updateIndex(index: List[EntryTree]): Unit = writeInFile(PathHelper.IndexFile, index.map(_.toString).mkString("\n"), shouldBeAppend = false)

  /**
   * Create the commit in memory
   *
   * @param commitToSave  the commit to save
   * @param currentBranch the branch where the commit should be saved
   * @return the commit created
   */
  def saveCommit(commitToSave: Commit, currentBranch: String): Commit = {

    //creation commit
    val commitCreated: Commit = commitToSave.copy()
    commitCreated.hash = createObject(commitCreated.buildContent)
    commitCreated.date = Calendar.getInstance().getTime().toString.replace(" ", "-")
    //commit added to the logs
    writeInFile(PathHelper.HeadLogFile, commitCreated.toString, shouldBeAppend = true)
    writeInFile(PathHelper.LogsDirectory + File.separator + currentBranch, commitCreated.toString, shouldBeAppend = true)
    //change the pointer commit of the branch
    writeInFile(PathHelper.BranchesDirectory + File.separator + currentBranch, commitCreated.hash, shouldBeAppend = false)

    commitCreated
  }

  /**
   *
   * @param path           : path of the file in which we want to write
   * @param content        : content to write in the file (everything in the file is deleted and replaced by this content)
   * @param shouldBeAppend : tells if the previous content of the file should be kept (shouldBeAppend==true) or deleted (shouldBeAppend==false)
   */
  private def writeInFile(path: String, content: String, shouldBeAppend: Boolean): Unit = {
    val file = new File(path)
    val bw = new BufferedWriter(new FileWriter(file, shouldBeAppend))
    bw.write(content)
    bw.close()
  }

}
