package dev.iurysouza.livematch.data.models.cloned.models.enums

import dev.iurysouza.livematch.data.models.cloned.models.base.Sorting

enum class SubmissionsSorting(

    override val requiresTimePeriod: Boolean = false,
    override val sortingStr: String,

    ) : Sorting {

    HOT(sortingStr = "hot"),
    BEST(sortingStr = "best"),
    NEW(sortingStr = "new"),
    RISING(sortingStr = "rising"),
    CONTROVERSIAL(true, "controversial"),
    TOP(true, "top")
}
