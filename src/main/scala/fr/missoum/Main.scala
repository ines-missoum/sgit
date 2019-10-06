package fr.missoum

import fr.missoum.utils.ConsolePrinter

object Main extends App {

  args match {
    case a: Array[String] if a.length == 0 => ConsolePrinter.noCommand()
    //branch
    case a: Array[String] if a.length == 2 && a(0).equals("branch") && a(1).equals("-av") => CommandExecutor.executeGetAllBranchesAndTags()
    case a: Array[String] if a.length == 2 && a(0).equals("branch") => CommandExecutor.executeCreateNewBranch(a(1))
    case a: Array[String] if a(0).equals("branch") => ConsolePrinter.notValidArguments("branch", "'branch -av' or 'branch <branch>'")
    //tag
    case a: Array[String] if a.length == 2 && a(0).equals("tag") => CommandExecutor.executeCreateNewTag(a(1))
    case a: Array[String] if a(0).equals("tag") => ConsolePrinter.notValidArguments("tag", "'tag <tag>'")
    //init
    case a: Array[String] if a(0).equals("init") => CommandExecutor.executeInit()
    case a: Array[String] => ConsolePrinter.notValidCommand(a(0))
  }

}