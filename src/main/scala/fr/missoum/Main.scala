package fr.missoum

import fr.missoum.utils.io.printers.{ConsolePrinter, ConsolePrinterImpl}

object Main {

  var executor: CommandExecutor = CommandExecutorImpl
  var printer: ConsolePrinter = ConsolePrinterImpl

  def main(args: Array[String]): Unit = {

    args match {
      //init
      case Array("init") => executor.executeInit()
      case Array("init", _*) => printer.notValidArguments("init", "just 'init'")
      //errors
      case Array(_*) if executor.isCommandForbiddenHere() => printer.notExistingSgitRepository()
      case Array() => printer.noCommand()
      //branch
      case Array("branch", "-av") => executor.executeGetAllBranchesAndTags()
      case Array("branch", x: String) => executor.executeCreateNewBranch(x)
      case Array("branch", _*) => printer.notValidArguments("branch", "'branch -av' or 'branch <branch>'")
      //tag
      case Array("tag", x: String) => executor.executeCreateNewTag(x)
      case Array("tag", _*) => printer.notValidArguments("tag", "'tag <tag>'")
      //add
      case a: Array[String] if a.length > 1 && a(0).equals("add") => executor.executeAdd(a.tail.toList)
      case Array("add") => printer.notValidArguments("add", "'add <file>' or 'add <file>*' ")
      //commit
      case Array("commit", "-m", x: String)  => executor.executeCommit(x)
      case Array("commit", _*) => printer.notValidArguments("commit", "'commit' -m <message>")
      //status
      case Array("status") => executor.executeStatus()
      case Array("status", _*) => printer.notValidArguments("status", "just 'status'")
      //log
      case Array("log") => executor.executeLog()
      case Array("log", "-p") => executor.executeLogP()
      case Array("log", _*) => printer.notValidArguments("log", "just 'log'")
      //checkout
      case Array("checkout", x: String) => executor.executeCheckout(x)
      case Array("checkout", _*) => printer.notValidArguments("checkout", "'checkout <branch or tag or commit hash>'")
      //diff
      case Array("diff") => executor.executeDiff()
      case Array("diff", _*) => printer.notValidArguments("diff", "just 'diff'")
      //default case
      case a: Array[String] => printer.notValidCommand(a(0))
    }
  }
}