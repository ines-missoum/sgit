package fr.missoum.utils
import java.lang.System._

object ConsolePrinter {

  def noCommand() = println("usage: sgit <command> [<args>]")

  def notValidCommand(wrongCommand:String) = println(wrongCommand +" is not a git command.")

  def sgitFolderAlreadyExists() = println("Reinitialized existing Git repository in " + System.getProperty("user.dir") + "/.sgit/")

  def sgitFolderCreated() = println("Initialized empty Git repository in " + System.getProperty("user.dir") + "/.sgit/")
}