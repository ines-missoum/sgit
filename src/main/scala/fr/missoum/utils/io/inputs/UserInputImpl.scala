package fr.missoum.utils.io.inputs

object UserInputImpl extends UserInput {
  def retrieveUserCommitMessage(): String = scala.io.StdIn.readLine()
}