package dev.iurysouza.livematch.reddit.data.models.entities.base

/**
 * Base interface for all items that be replied to,
 * including Submission and Comment.
 *
 * @property fullname Fullname of the item, e.g. "t1_c3v7f8u"
 *
 */
interface Replyable {

  val fullname: String
}
