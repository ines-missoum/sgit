package fr.missoum.commands
import fr.missoum.logic.EntryTree

trait SgitCommandHelper{
  protected def inFirstListButNotInSecondByPath(l1: List[EntryTree], l2: List[EntryTree]): List[EntryTree] = {
    l1.filter(x => !l2.exists(y => x.path.equals(y.path)))
  }

  protected def inFirstListButNotInSecondByAll(l1: List[EntryTree], l2: List[EntryTree]): List[EntryTree] = {
    l1.filter(x => !l2.exists(y => x.hash.equals(y.hash) && x.path.equals(y.path)))
  }

  protected def getModifiedElements(l1: List[EntryTree], l2: List[EntryTree]): List[EntryTree] = {
    l1.filter(x => l2.exists(y => x.path.equals(y.path) && !x.hash.equals(y.hash)))
  }
}