package fr.missoum.utils

object ConsolePrinter {

  def sgitFolderAlreadyExists() ={
    println ("Reinitialized existing Git repository in /????/.sgit/")
  }

  def sgitFolderCreated() ={
    println ("Initialized empty Git repository in /????/.sgit/")
  }
}