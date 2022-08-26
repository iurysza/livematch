package dev.iurysouza.livematch.data.models.reddit.entities.enums

import dev.iurysouza.livematch.data.models.reddit.entities.base.Sorting

enum class SearchSorting(

    override val requiresTimePeriod: Boolean = false,
    override val sortingStr: String,

    ) : Sorting {

    RELEVANCE(true, sortingStr = "relevance"),
    HOT(sortingStr = "hot"),
    NEW(sortingStr = "new"),
    TOP(true, "top"),
    COMMENTS(true, "comments")
}
