package fr.missoum

import fr.missoum.utils.helpers.PathHelper
import fr.missoum.utils.io.ConsolePrinter

object Main {

  var executor :CommandExecutor = CommandExecutorImpl
  def main(args: Array[String]): Unit = {

    args match {
      //init
      case Array("init") => executor.executeInit()
      case Array("init", _*) => ConsolePrinter.notValidArguments("init", "just 'init'")
      //errors
      case Array(_*) if CommandExecutorImpl.isCommandForbiddenHere() => ConsolePrinter.notExistingSgitRepository()
      case Array() => ConsolePrinter.noCommand()
      //branch
      case Array("branch", "-av") => CommandExecutorImpl.executeGetAllBranchesAndTags()
      case Array("branch", x: String) => CommandExecutorImpl.executeCreateNewBranch(x)
      case Array("branch", _*) => ConsolePrinter.notValidArguments("branch", "'branch -av' or 'branch <branch>'")
      //tag
      case Array("tag", x: String) => CommandExecutorImpl.executeCreateNewTag(x)
      case Array("tag", _*) => ConsolePrinter.notValidArguments("tag", "'tag <tag>'")
      //add
      case a: Array[String] if a.length > 1 && a(0).equals("add") => CommandExecutorImpl.executeAdd(a.tail, "")
      case Array("add") => ConsolePrinter.notValidArguments("add", "'add <file>' or 'add <file>*' ")

      case a: Array[String] => ConsolePrinter.notValidCommand(a(0))
    }
  }
}