package fr.missoum.commands
import fr.missoum.logic.EntryTree

trait SgitCommandHelper{
  protected def inFirstListButNotInSecond(l1: Array[EntryTree], l2: Array[EntryTree]): Array[EntryTree] = {
    l1.filter(x => !l2.exists(y => x.path.equals(y.path)))
  }

  protected def getModifiedElements(l1: Array[EntryTree], l2: Array[EntryTree]): Array[EntryTree] = {
    l1.filter(x => l2.exists(y => x.path.equals(y.path) && !x.hash.equals(y.hash)))
  }
}