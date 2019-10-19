package fr.missoum.commands

import fr.missoum.logic.Commit
import fr.missoum.utils.io.readers.SgitReader

trait SgitLog {

  /**
   * Retrieves all the commit of the current branch
   *
   * @param logsContent the content of the logfile
   * @return the list of all the commits
   */
  def retrieveAllCommits(logsContent: String): List[Commit]
}