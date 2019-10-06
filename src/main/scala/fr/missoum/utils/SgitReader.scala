package fr.missoum.utils
import java.io._

object SgitReader{

  def isExistingSgitFolder() : Boolean = {
    (new File(".sgit")).isDirectory()
  }
}