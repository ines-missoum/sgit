package fr.missoum.commands

import fr.missoum.logic.Commit
import fr.missoum.utils.io.readers.SgitReader

trait SgitLog{

  var sgitReader: SgitReader

  /**
   * Retrieves all the commit of the current branch
   *
   * @return the list of all the commits
   */
  def retrieveAllCommits(): Array[Commit]
}