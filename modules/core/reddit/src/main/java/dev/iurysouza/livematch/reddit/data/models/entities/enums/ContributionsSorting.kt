package dev.iurysouza.livematch.reddit.data.models.entities.enums

import dev.iurysouza.livematch.reddit.data.models.entities.base.Sorting

enum class ContributionsSorting(

  override val requiresTimePeriod: Boolean = false,
  override val sortingStr: String,

) : Sorting {

  HOT(sortingStr = "hot"),
  NEW(sortingStr = "new"),
  CONTROVERSIAL(true, "controversial"),
  TOP(true, "top"),
}
