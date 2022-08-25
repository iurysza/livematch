package dev.iurysouza.livematch.data.models.cloned.models.enums

import dev.iurysouza.livematch.data.models.cloned.models.base.Sorting

enum class ContributionsSorting(

    override val requiresTimePeriod: Boolean = false,
    override val sortingStr: String,

    ) : Sorting {

    HOT(sortingStr = "hot"),
    NEW(sortingStr = "new"),
    CONTROVERSIAL(true, "controversial"),
    TOP(true, "top")
}
