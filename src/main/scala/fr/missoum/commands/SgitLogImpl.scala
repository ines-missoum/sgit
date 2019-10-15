package fr.missoum.commands

import fr.missoum.logic.Commit
import fr.missoum.utils.io.readers.{SgitReader, SgitReaderImpl}

object SgitLogImpl extends SgitLog {

  var sgitReader: SgitReader = SgitReaderImpl

  /**
   * Retrieves all the commit of the current branch
   *
   * @return the list of all the commits
   */
  def retrieveAllCommits(): List[Commit] = {

    if (sgitReader.getLog().isEmpty)
      List[Commit]()
    else {
      val logs = sgitReader.getLog().split("\n").toList
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