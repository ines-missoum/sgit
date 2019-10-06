package fr.missoum.utils
import java.io._

/**
 * This object is responsible for all writing actions. Which means create files and folders and update them.
 */
object SgitWriter{

  /**
   * Creates the sgit directory where the command has been executed. It contains all the folders and files necessary.
   * It also creates the master branch and checkout on the master branch.
   */
  def createSgitRepository() ={

    //creation of all files and folders
    val listFolders = List(".sgit/logs/refs/heads", ".sgit/refs/tags", ".sgit/refs/heads",".sgit/objects")
    val listFiles = List(".sgit/logs/HEAD", ".sgit/HEAD", ".sgit/index")
    listFolders.map(new File(_).mkdirs())
    listFiles.map(new File(_).createNewFile)

    //creation of the master branch and checkout
    createNewBranch("master")
    setHeadBranch("master")
  }

  /**
   * Deletes everything in the HEAD file located on top of the .sgit repository and add a line with the name of the branch in parameter.
   * To use this function be sure that the .sgit repository and the parameter branch exist.
   * @param branch : the branch to checkout
   */
  def setHeadBranch(branch:String): Unit ={
    val file = new File(".sgit/HEAD")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write("master")
    bw.close()
  }

  /**
   * Creates the parameter branch.
   * Which means creates all the necessary files for this branch (two files named with the branch name in .sgit/refs/heads/ and .sgit/logs/refs/heads/)
   * To use this function be sure that the .sgit repository exists.
   * @param newBranch : the branch to create
   */
  def createNewBranch(newBranch:String):Unit ={
    (new File(".sgit/refs/heads/"+newBranch)).createNewFile
    (new File(".sgit/logs/refs/heads/"+newBranch)).createNewFile
  }

  /**
   * Creates the parameter tag.
   * Which means creates all the necessary files for this branch (one file named with the tag name in .sgit/refs/tags/)
   * To use this function be sure that the .sgit repository exists.
   * @param newTag : the tag to create
   */
  def createNewTag(newTag:String):Unit ={
    (new File(".sgit/refs/tags/"+newTag)).createNewFile
  }
}