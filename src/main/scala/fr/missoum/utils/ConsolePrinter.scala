package fr.missoum.utils
import java.lang.System._

/**
 * Object responsible for communication with the user. This means print messages on the console.
 */
object ConsolePrinter {

  def noCommand() = println("usage: sgit <command> [<args>]")
  def notValidCommand(wrongCommand:String) = println(wrongCommand +" is not a sgit command.")
  def sgitFolderAlreadyExists() = println("Reinitialized existing Sgit repository in " + System.getProperty("user.dir") + "/.sgit/")
  def sgitFolderCreated() = println("Initialized empty Sgit repository in " + System.getProperty("user.dir") + "/.sgit/")
  def branchCreated(newBranch:String)=println("Branch '"+newBranch+"' created.")
  def branchAlreadyExists(existingBranch:String)=println("fatal: A branch named '"+existingBranch+"' already exists.")

}