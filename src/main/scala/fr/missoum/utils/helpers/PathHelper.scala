package fr.missoum.utils.helpers

//import scala.reflect.io.File

import java.io.File

object PathHelper {

  //base path
  val SgitRepositoryName = ".sgit"
  /**
   * Gives the path of the sgit repository if it exists else an empty String
   */
  val SgitPath: String = {
    val executingDirectory = new File(System.getProperty("user.dir"))
    val path = findSgitPathFrom(executingDirectory)
    if (path.isEmpty) "" else path.get + File.separator
  }

  /**
   * Gives the path of the sgit repository if it exists in the directory given in parameter or in any of its parent directories
   *
   * @param fromDirectory : Directory where beginning to search
   * @return The path of the sgit repository if it exists, otherwise None
   */
  def findSgitPathFrom(fromDirectory: File): Option[String] = {

    if (fromDirectory == null || !fromDirectory.isDirectory)
      None

    else {
      val sgitFolderExistsHere = (new File(fromDirectory.getAbsolutePath + File.separator + SgitRepositoryName)).exists()
      if (sgitFolderExistsHere)
        Some(fromDirectory.getAbsolutePath)
      else
        findSgitPathFrom(fromDirectory.getParentFile)
    }

  }

  //Directories absolute paths
  val ObjectDirectory = SgitPath + SgitRepositoryName + File.separator + "objects"
  val TagsDirectory = SgitPath + SgitRepositoryName + File.separator + "refs" + File.separator + "tags"
  val BranchesDirectory = SgitPath + SgitRepositoryName + File.separator + "refs" + File.separator + "heads"
  val LogsDirectory = SgitPath + SgitRepositoryName + File.separator + "logs" + File.separator + "refs" + File.separator + "heads"

  //files absolute path
  val IndexFile = SgitPath + SgitRepositoryName + File.separator + "index"
  val HeadFile = SgitPath + SgitRepositoryName + File.separator + "HEAD"
  val HeadLogFile = SgitPath + SgitRepositoryName + File.separator + "logs" + File.separator + "HEAD"

  def getSimplePathOfFile(absoluteFilePath: String): String = absoluteFilePath.toSeq.diff(SgitPath.toSeq).unwrap

  def getAbsolutePathOfFile(simplePath: String): String = SgitPath + simplePath
}