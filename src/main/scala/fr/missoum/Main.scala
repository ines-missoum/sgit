package fr.missoum;
import fr.missoum.utils.{ConsolePrinter, SgitReader, SgitWriter}

object Main extends App {

  if(args.length==0)
    ConsolePrinter.noCommand()
  else
    //check if it's a valid command
  if(args(0).equals("init")){
    if(SgitReader.isExistingSgitFolder())
      ConsolePrinter.sgitFolderAlreadyExists()
    else {
      SgitWriter.createSgitRepository()
      ConsolePrinter.sgitFolderCreated()
    }
  }
  else
  ConsolePrinter.notValidCommand(args(0))




}