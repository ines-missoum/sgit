package fr.missoum.commands

import fr.missoum.logic.Commit
import fr.missoum.utils.io.readers.{SgitReader, SgitReaderImpl}

object SgitLogImpl extends SgitLog {

  /**
   * Retrieves all the commit of the current branch
   *
   * @param logsContent the content of the logfile
   * @return the list of all the commits
   */
  def retrieveAllCommits(logsContent: String): List[Commit] = {

    if (logsContent.isEmpty)
      List[Commit]()
    else {
      val logs = logsContent.split("\n").toList
      retrieveAllCommitRec(logs)
    }

  }

  def retrieveAllCommitRec(logs: List[String]): List[Commit] = {
    if (logs.isEmpty)
      List[Commit]()
    else
      Commit.getCommitFromToString(logs(0) + "\n" + logs(1)) +: retrieveAllCommitRec(logs.drop(2))
  }

}