package fr.missoum.utils.io.readers

object WorkspaceReaderImpl extends WorkspaceReader {
   def fileExists(path: String): Boolean = scala.reflect.io.File(path).exists
}
