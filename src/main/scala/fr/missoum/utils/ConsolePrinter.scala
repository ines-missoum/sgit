package fr.missoum.utils
import java.lang.System._

object ConsolePrinter {

  def sgitFolderAlreadyExists() ={
    println ("Reinitialized existing Git repository in "+System.getProperty("user.dir")+"/.sgit/")
  }

  def sgitFolderCreated() ={
    println ("Initialized empty Git repository in "+System.getProperty("user.dir")+"/.sgit/")
  }
}