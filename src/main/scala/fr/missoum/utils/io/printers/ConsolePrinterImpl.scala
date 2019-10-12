package fr.missoum.utils.io.printers

import fr.missoum.logic.EntryTree
import fr.missoum.utils.helpers.PathHelper

/**
 * Object responsible for communication with the user. This means print messages on the console.
 */
object ConsolePrinterImpl extends ConsolePrinter {

  def fileNotExist(fileName: String): Unit = println("fatal: '" + fileName + "' did not match any files")

  def notExistingSgitRepository() = println("fatal: not a sgit repository (or any of the parent directories): .sgit")

  def noCommand() = println("usage: sgit <command> [<args>]")

  def notValidCommand(wrongCommand: String) = println(wrongCommand + " is not a sgit command.")

  def notValidArguments(command: String, possibleInput: String) = println("The command '" + command + "' cannot accept those arguments. \nTrie " + possibleInput)

  def sgitFolderAlreadyExists() = println("Reinitialized existing Sgit repository in " + PathHelper.SgitPath + ".sgit/")

  def sgitFolderCreated() = println("Initialized empty Sgit repository in " + System.getProperty("user.dir") + "/.sgit/")

  def branchCreated(newBranch: String) = println("Branch '" + newBranch + "' created.")

  def branchAlreadyExists(existingBranch: String) = println("fatal: A branch named '" + existingBranch + "' already exists.")

  def tagCreated(newTag: String) = println("Tag '" + newTag + "' created.")

  def tagAlreadyExists(existingTag: String) = println("fatal: tag '" + existingTag + "' already exists.")

  def printBranchesAndTags(currentBranch: String, tags: Array[String], branches: Array[String]) = {
    var result = ""
    branches.map(x => if (x.equals(currentBranch)) result += "* " + x + "\n" else result += x + "\n")
    println("__BRANCHES__ \n" + result)
    if (tags.length != 0)
      println("__TAGS__ \n" + tags.mkString("\n"))

  }

  def askEnterMessageCommits() = println("Please enter the commit message for your changes : ")

  def commitCreatedMessage(branch: String, message: String, nbFilesChanged: Int) = println("\n[" + branch + "] " + message + " \n" + nbFilesChanged + " file(s) changed")

  def nothingToCommit(branch: String) = println("On branch " + branch + "\nnothing to commit, working tree clean")

  def untrackedFiles(untrackedFiles: Array[String]) = {
    val files = untrackedFiles.map("\t" + _).mkString("\n")
    println("Untracked files:\n  (use \"git add <file>...\" to include in what will be committed)\n\n" + Console.RED + files)
  }

}
