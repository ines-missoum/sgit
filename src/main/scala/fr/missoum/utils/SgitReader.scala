package fr.missoum.utils
import java.io._

/**
 * This object is responsible for accessing (read only) files or directories.
 */
object SgitReader{

  def isExistingSgitFolder() : Boolean = {
    (new File(".sgit")).isDirectory()
  }
}