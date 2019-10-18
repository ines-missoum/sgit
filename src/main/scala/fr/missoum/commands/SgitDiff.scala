package fr.missoum.commands

trait SgitDiff {

  /**
   *
   * @param contentFile1 the old file
   * @param contentFile2 the new content of the file
   * @return the old file with the diff lines in the right place
   */
  def diff(contentFile1: List[String], contentFile2: List[String]): List[String]
}