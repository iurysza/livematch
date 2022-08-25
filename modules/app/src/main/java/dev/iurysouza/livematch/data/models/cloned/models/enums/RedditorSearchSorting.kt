package dev.iurysouza.livematch.data.models.cloned.models.enums

import dev.iurysouza.livematch.data.models.cloned.models.base.Sorting

enum class RedditorSearchSorting(

    override val requiresTimePeriod: Boolean = false,
    override val sortingStr: String,

    ) : Sorting {

    RELEVANCE(false, "relevance"),
    ALL(false, "all")
}
