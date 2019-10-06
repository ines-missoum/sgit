package fr.missoum.utils

import java.lang.System._

/**
 * Object responsible for communication with the user. This means print messages on the console.
 */
object ConsolePrinter {

  def noCommand() = println("usage: sgit <command> [<args>]")

  def notValidCommand(wrongCommand: String) = println(wrongCommand + " is not a sgit command.")

  def notValidArguments(command: String, possibleInput: String) = println("The command '" + command + "' cannot accept those arguments. \nTrie " + possibleInput)

  def sgitFolderAlreadyExists() = println("Reinitialized existing Sgit repository in " + System.getProperty("user.dir") + "/.sgit/")

  def sgitFolderCreated() = println("Initialized empty Sgit repository in " + System.getProperty("user.dir") + "/.sgit/")

  def branchCreated(newBranch: String) = println("Branch '" + newBranch + "' created.")

  def branchAlreadyExists(existingBranch: String) = println("fatal: A branch named '" + existingBranch + "' already exists.")

  def tagCreated(newTag: String) = println("Tag '" + newTag + "' created.")

  def tagAlreadyExists(existingTag: String) = println("fatal: tag '" + existingTag + "' already exists.")

  def printBranchesAndTags(currentBranch: String, tags: Array[String], branches: Array[String]) = {
    var result = ""
    branches.map(x => if (x.equals(currentBranch)) result = result + "* " + x + "\n" else result = result + x + "\n")
    println("__BRANCHES__ \n\n" + result)
    if (tags.length != 0)
      println("__TAGS__ \n\n" + tags.mkString("\n"))

  }

}