package dev.iurysouza.livematch.reddit.data.models.entities.base

import dev.iurysouza.livematch.reddit.data.models.entities.commons.Gildings

/**
 * Base interface for all the item that can be given Gildings
 *
 * @property canGild whether this item can be given a gilding or not.
 *
 * @property gildings the number of times this comment received reddit gold, silver, platinum.
 *
 */
interface Gildable : Thing {

  override val id: String

  override val fullname: String

  val canGild: Boolean

  val gildings: Gildings
}
