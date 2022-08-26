package dev.iurysouza.livematch.data.models.reddit.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.data.models.reddit.responses.base.Envelope
import dev.iurysouza.livematch.data.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.data.models.reddit.entities.base.RedditorData

@JsonClass(generateAdapter = true)
class EnvelopedRedditorData(

    @Json(name = "kind")
    override val kind: EnvelopeKind,

    @Json(name = "data")
    override val data: RedditorData,

    ) : Envelope<RedditorData>
