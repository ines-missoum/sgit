package fr.missoum;
import fr.missoum.utils.{ConsolePrinter, SgitReader, SgitWriter}

object Main extends App {

  args match {
    case a:Array[String] if a.length == 0  => ConsolePrinter.noCommand()
    case a:Array[String] if a(0).equals("init") => {
      if (SgitReader.isExistingSgitFolder())
        ConsolePrinter.sgitFolderAlreadyExists()
      else {
        SgitWriter.createSgitRepository()
        ConsolePrinter.sgitFolderCreated()
      }
    }
    case a:Array[String] => ConsolePrinter.notValidCommand(a(0))
  }

}