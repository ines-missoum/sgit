package fr.missoum;
import fr.missoum.utils.ConsolePrinter

object Main extends App {

  args match {
    case a:Array[String] if a.length == 0  => ConsolePrinter.noCommand()
    case a:Array[String] if a.length == 2 & a(0).equals("branch") & a(1).equals("-av") => CommandExecutor.executeGetAllBranchesAndInfo()
    case a:Array[String] if a.length == 2 & a(0).equals("branch") => CommandExecutor.executeCreateNewBranch(a(1))
    case a:Array[String] if a.length == 1 & a(0).equals("branch") => CommandExecutor.executeGetAllBranches()
    case a:Array[String] if a(0).equals("init") => CommandExecutor.executeInit()
    case a:Array[String] => ConsolePrinter.notValidCommand(a(0))
  }

}