package dev.iurysouza.livematch.reddit.data.models.entities.enums

import dev.iurysouza.livematch.reddit.data.models.entities.base.Sorting

enum class RedditorSearchSorting(

  override val requiresTimePeriod: Boolean = false,
  override val sortingStr: String,

) : Sorting {

  RELEVANCE(false, "relevance"),
  ALL(false, "all"),
}
