package fr.missoum.utils
import java.io._

object SgitWriter{

  def createSgitRepository() ={

    //creation of logs files and folders
    (new File(".sgit/logs/refs/heads")).mkdirs()
    (new File(".sgit/logs/refs/heads/master")).createNewFile
    (new File(".sgit/logs/HEAD")).createNewFile

    //creation of refs files and folders
    (new File(".sgit/refs/tags")).mkdirs()
    (new File(".sgit/refs/heads")).mkdir()
    (new File(".sgit/refs/heads/HEAD")).createNewFile()

    //creation of objects files and folders
    (new File(".sgit/objects")).mkdir()

    //creation of files in top of .sgit folder
    (new File(".sgit/HEAD")).createNewFile
    (new File(".sgit/index")).createNewFile

  }
}