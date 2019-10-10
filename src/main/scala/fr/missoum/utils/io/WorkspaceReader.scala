package  fr.missoum.utils.io

object WorkspaceReader{
   def fileExists(path: String): Boolean = scala.reflect.io.File(path).exists
}