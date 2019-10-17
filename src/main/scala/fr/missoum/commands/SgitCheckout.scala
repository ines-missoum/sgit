package fr.missoum.commands

import fr.missoum.logic.EntryTree

trait SgitCheckout extends SgitCommandHelper {

  /**
   * Check if the checkout is allowed <=> if it doesn't exist files modified since the switch commit but not committed
   * @param lastCommittedBlobs the blobs committed in the last commit
   * @param index the current index
   * @param checkoutBlobs the index of the switch commit
   * @return an empty list if the checkout is allowed, otherwise the list path files that forbid the checkout
   */
  def checkoutNotAllowedOn(lastCommittedBlobs: List[EntryTree], index: List[EntryTree], checkoutBlobs: List[EntryTree]): List[String]

}