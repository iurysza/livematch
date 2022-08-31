package dev.iurysouza.livematch.domain.models.reddit.entities.enums

import dev.iurysouza.livematch.domain.models.reddit.entities.base.Sorting

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
