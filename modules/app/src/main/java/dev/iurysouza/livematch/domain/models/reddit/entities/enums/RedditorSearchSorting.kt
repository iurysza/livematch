package dev.iurysouza.livematch.domain.models.reddit.entities.enums

import dev.iurysouza.livematch.domain.models.reddit.entities.base.Sorting

enum class RedditorSearchSorting(

    override val requiresTimePeriod: Boolean = false,
    override val sortingStr: String,

    ) : Sorting {

    RELEVANCE(false, "relevance"),
    ALL(false, "all")
}
