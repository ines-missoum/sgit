package fr.missoum.utils.io.printers

import fr.missoum.logic.Commit
import fr.missoum.utils.helpers.PathHelper

/**
 * Object responsible for communication with the user. This means print messages on the console.
 */
object ConsolePrinterImpl extends ConsolePrinter {

  def fileNotExist(fileName: String): Unit = println("fatal: '" + fileName + "' did not match any files")

  def notExistingSgitRepository(): Unit = println("fatal: not a sgit repository (or any of the parent directories): .sgit")

  def noCommand(): Unit = println("usage: sgit <command> [<args>]")

  def notValidCommand(wrongCommand: String): Unit = println(wrongCommand + " is not a sgit command.")

  def notValidArguments(command: String, possibleInput: String): Unit = println("The command '" + command + "' cannot accept those arguments. \nTrie " + possibleInput)

  def sgitFolderAlreadyExists(): Unit = println("Reinitialized existing Sgit repository in " + PathHelper.SgitPath + ".sgit/")

  def sgitFolderCreated(): Unit = println("Initialized empty Sgit repository in " + System.getProperty("user.dir") + "/.sgit/")

  def branchCreated(newBranch: String): Unit = println("Branch '" + newBranch + "' created.")

  def branchAlreadyExists(existingBranch: String): Unit = println("fatal: A branch named '" + existingBranch + "' already exists.")

  def tagCreated(newTag: String): Unit = println("Tag '" + newTag + "' created.")

  def tagAlreadyExists(existingTag: String): Unit = println("fatal: tag '" + existingTag + "' already exists.")

  def printBranchesAndTags(currentBranch: String, tags: List[String], branches: List[String]): Unit = {
    var result = ""
    branches.map(x => if (x.equals(currentBranch)) result += "* " + x + "\n" else result += x + "\n")
    println("__BRANCHES__ \n" + result)
    if (tags.nonEmpty)
      println("__TAGS__ \n" + tags.mkString("\n"))

  }

  def askEnterMessageCommits(): Unit = println("Please enter the commit message for your changes : ")

  def commitCreatedMessage(branch: Option[String], message: String, nbFilesChanged: Int): Unit = println("\n[" + branch.getOrElse("detached head") + "] " + message + " \n" + nbFilesChanged + " file(s) changed")

  def nothingToCommit(branch: Option[String]): Unit = println("On branch " + branch.getOrElse("[detached head]") + "\nnothing to commit, working tree clean")

  def untrackedFiles(untrackedFiles: List[String]): Unit = {
    val files = untrackedFiles.map("\t" + _).mkString("\n")
    println("Untracked files:\n  (use \"git add <file>...\" to include in what will be committed)\n\n" + Console.RED + files + "\n" + Console.WHITE)
  }

  def changesNotStagedForCommit(modifiedNotStaged: List[String], deletedNotStaged: List[String]): Unit = {
    val deleted = deletedNotStaged.map("\tdeleted:     " + _).mkString("\n")
    val modified = modifiedNotStaged.map("\tmodified:    " + _).mkString("\n")
    println("Changes not staged for commit:\n  (use \"git add <file>...\" to update what will be committed)\n\n"
      + Console.RED + deleted + "\n" + modified + "\n" + Console.WHITE)
  }

  def changesToBeCommitted(news: List[String], modified: List[String], deleted: List[String]): Unit = {
    val newsPrint = news.map("\tnew file:    " + _).mkString("\n")
    val deletedPrint = deleted.map("\tdeleted:     " + _).mkString("\n")
    val modifiedPrint = modified.map("\tmodified:    " + _).mkString("\n")
    println("Changes to be committed:\n\n"
      + Console.GREEN + newsPrint + "\n" + deletedPrint + "\n" + modifiedPrint + "\n" + Console.WHITE)
  }

  def branch(branch: String): Unit = {
    println("On branch " + branch)
  }

  def displayAllCommits(commits: List[Commit], branch: String): Unit = {
    val allCommits = commits.map(c =>
      Console.YELLOW + "commit " + c.hash + "(" + Console.CYAN + "HEAD -> " + Console.GREEN + branch + Console.YELLOW + ")" +
        Console.WHITE + "\nAuthor: " + c.author + "\nDate: " + c.date + "\n\n\t" + c.message + "\n")
      .mkString("\n")
    println(allCommits)
  }

  def displaySingleCommit(c: Commit, branch: String): Unit = {
    println(Console.YELLOW + "commit " + c.hash + "(" + Console.CYAN + "HEAD -> " + Console.GREEN + branch + Console.YELLOW + ")" +
      Console.WHITE + "\nAuthor: " + c.author + "\nDate: " + c.date + "\n\n\t" + c.message + "\n")
  }

  def noLog(branch: String): Unit = println("fatal: your current branch '" + branch + "' does not have any commits yet")

  def statusAllGood(): Unit = println("nothing to commit, working tree clean")

  def notExistingSwitch(switchTo: String): Unit = println("error: pathspec '" + switchTo + "' did not match any file(s) known to sgit.")

  def notAllowedCheckout(modifiedFiles: List[String]): Unit = {
    val printFiles = modifiedFiles.map("\n\t" + _).mkString("\n")
    println("error: Your local changes to the following files would be overwritten by checkout:" + printFiles + "Please commit your changes or stash them before you switch branches.\nAborting")
  }

  def printSingleDiff(path: String, value: List[String]): Unit = {
    val diff = value.map(x => {
      if (x.startsWith("-")) Console.RED + x
      else if (x.startsWith("+")) Console.GREEN + x
      else Console.WHITE + x
    }).mkString("\n") + "\n" + Console.WHITE
    println("______" + path + "______\n" + diff + "\n")
  }

  def noCreationPossible(): Unit = println("fatal: you need to do a first commit before creating a branch or a tag")

  def improperSgitRepository(): Unit = println("fatal: improper sgit repository")

  def notOnBranch(): Unit = println("fatal: you are not on a branch")

  def checkoutBranch(head: String): Unit = println("Switched to '" + head + "'")

  def detachedHead(): Unit = println("You are in 'detached HEAD' state. You can look around, make experimental\nchanges and commit them, and you can discard any commits you make in this\nstate without impacting any branches by performing another checkout.\n\nIf you want to create a new branch to retain commits you can do so.")

  def noLogP(branch: String): Unit = println("failure : You only have one commit on "+branch+", no diff to display.")
}
