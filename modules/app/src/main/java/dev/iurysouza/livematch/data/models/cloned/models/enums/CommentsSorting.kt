package dev.iurysouza.livematch.data.models.cloned.models.enums

import dev.iurysouza.livematch.data.models.cloned.models.base.Sorting

enum class CommentsSorting(

    override val requiresTimePeriod: Boolean = false,
    override val sortingStr: String,

    ) : Sorting {

    BEST(sortingStr = "best"),
    CONFIDENCE(sortingStr = "confidence"),
    TOP(sortingStr = "top"),
    NEW(sortingStr = "new"),
    CONTROVERSIAL(sortingStr = "controversial"),
    OLD(sortingStr = "old"),
    RANDOM(sortingStr = "random"),
    QA(sortingStr = "qa"),
    LIVE(sortingStr = "live")
}
