package fr.missoum.utils
import java.io._

/**
 * This object is responsible for accessing (read only) files or directories and doing test on it.
 */
object SgitReader{

  def isExistingSgitFolder() : Boolean = {
    (new File(".sgit")).isDirectory()
  }

  def isExistingBranch(branch:String) : Boolean = {
    (new File(".sgit/refs/heads/"+branch)).isFile()
  }
}