package fr.missoum.utils

import java.io._

import fr.missoum.HashHelper

/**
 * This object is responsible for all writing actions. Which means create files and folders and update them.
 */
object SgitWriter {

  /**
   * Creates the sgit directory where the command has been executed. It contains all the folders and files necessary.
   * It also creates the master branch and checkout on the master branch.
   */
  def createSgitRepository() = {

    //creation of all files and folders
    val listFolders = List(".sgit/logs/refs/heads", ".sgit/refs/tags", ".sgit/refs/heads", ".sgit/objects")
    val listFiles = List(".sgit/logs/HEAD", ".sgit/HEAD", ".sgit/index", ".sgit/location")
    listFolders.map(new File(_).mkdirs())
    listFiles.map(new File(_).createNewFile)

    //creation of the master branch and checkout
    createNewBranch("master")
    setHeadBranch("master")

    //set location .sgit
    setLocation()
  }

  /**
   * Deletes everything in the HEAD file located on top of the .sgit repository and add a line with the name of the branch in parameter.
   * To use this function be sure that the .sgit repository and the parameter branch exist.
   *
   * @param branch : the branch to checkout
   */
  def setHeadBranch(branch: String): Unit = {
    writeInFile(".sgit/HEAD", "master", false)

  }

  /**
   * Save the creation location of .sgit repository
   */
  def setLocation() = writeInFile(".sgit/location", System.getProperty("user.dir"), false)


  /**
   * Creates the parameter branch.
   * Which means creates all the necessary files for this branch (two files named with the branch name in .sgit/refs/heads/ and .sgit/logs/refs/heads/)
   * To use this function be sure that the .sgit repository exists.
   *
   * @param newBranch : the branch to create
   */
  def createNewBranch(newBranch: String): Unit = {
    (new File(".sgit/refs/heads/" + newBranch)).createNewFile
    (new File(".sgit/logs/refs/heads/" + newBranch)).createNewFile
  }

  /**
   * Creates the parameter tag.
   * Which means creates all the necessary files for this branch (one file named with the tag name in .sgit/refs/tags/)
   * To use this function be sure that the .sgit repository exists.
   *
   * @param newTag : the tag to create
   */
  def createNewTag(newTag: String): Unit = {
    (new File(".sgit/refs/tags/" + newTag)).createNewFile
  }

  /**
   * Creates the blob in the .sgit repository if it doesn't already exists
   *
   * @param contentFile content of the file for which we want to create the blob
   */
  def createBlob(contentFile: String) = {

    //retrieves path from hash of content file
    val hash = HashHelper.hashFile(contentFile)
    val pathFolder = ".sgit/objects/" + hash.substring(0, 2)
    val pathFile = pathFolder + "/" + hash.substring(2)

    //creation of folder and file if do not exist
    new File(pathFolder).mkdir()
    val isNew = new File(pathFile).createNewFile
    if (isNew) {
      writeInFile(pathFile, contentFile, false)
    }
  }

  def addToIndex(contentToAdd: String) = writeInFile(".sgit/index", contentToAdd, true)

  def buildIndexLine(hash: String, pathFile: String) = hash + " " + pathFile + "\n" //TODO : put in another class

  /**
   *
   * @param path           : path of the file in which we want to write
   * @param content        : content to write in the file (everything in the file is deleted and replaced by this content)
   * @param shouldBeAppend : tells if the previous content of the file should be kept (shouldBeAppend==true) or deleted (shouldBeAppend==false)
   */
  def writeInFile(path: String, content: String, shouldBeAppend: Boolean) = {
    val file = new File(path)
    val bw = new BufferedWriter(new FileWriter(file, shouldBeAppend))
    bw.write(content)
    bw.close()
  }

}