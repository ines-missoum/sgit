package fr.missoum.commands

import fr.missoum.logic.EntryTree

object SgitCheckoutImpl extends SgitCheckout {

  /**
   * Check if the checkout is allowed <=> if it doesn't exist files modified since the switch commit but not committed
   *
   * @param lastCommittedBlobs the blobs committed in the last commit
   * @param index              the current index
   * @param checkoutBlobs      the index of the switch commit
   * @return an empty list if the checkout is allowed, otherwise the list path files that forbid the checkout
   */
  def checkoutNotAllowedOn(lastCommittedBlobs: List[EntryTree], index: List[EntryTree], checkoutBlobs: List[EntryTree]): List[String] = {

    val modifiedFilesToSwitch = getModifiedElements(index, checkoutBlobs).map(_.path)
    //we retrieve all the modified and new files that are not committed
    val modifiedButNotCommitted = getModifiedElements(index, lastCommittedBlobs).map(_.path) ++ inFirstListButNotInSecondByPath(index, lastCommittedBlobs).map(_.path)

    modifiedFilesToSwitch.filter(x => modifiedButNotCommitted.exists(y => x.equals(y)))
  }

  /**
   * Retrieve all the versions of blobs that shouldn't exist in the switch <=> the blobs to delete
   * @param currentIndex list of blobs of the current index
   * @param checkoutBlobs list of blobs of the switch
   * @return all the path to delete in the workspace
   */
  def findFilesToDelete(currentIndex: List[EntryTree], checkoutBlobs: List[EntryTree]): List[EntryTree] = {
    inFirstListButNotInSecondByAll(currentIndex, checkoutBlobs)
  }

  /**
   *
   * @param currentIndex list of blobs of the current index
   * @param checkoutBlobs list of blobs of the switch
   * @return all the path to create in the workspace
   */
  def findFilesToCreate(currentIndex: List[EntryTree], checkoutBlobs: List[EntryTree]): List[EntryTree]={
    inFirstListButNotInSecondByAll(checkoutBlobs,currentIndex)
  }
}