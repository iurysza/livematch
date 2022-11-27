package dev.iurysouza.livematch.domain.models.reddit.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.domain.models.reddit.entities.TrophyList
import dev.iurysouza.livematch.domain.models.reddit.responses.base.EnvelopeKind

@JsonClass(generateAdapter = true)
class EnvelopedTrophyList(

    @Json(name = "kind")
    val kind: EnvelopeKind,

    @Json(name = "data")
    val data: TrophyList,

    )
