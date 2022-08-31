package dev.iurysouza.livematch.domain.models.reddit.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.domain.models.reddit.responses.base.Envelope
import dev.iurysouza.livematch.domain.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.domain.models.reddit.entities.Multi

@JsonClass(generateAdapter = true)
class EnvelopedMulti(

    @Json(name = "kind")
    override val kind: EnvelopeKind,

    @Json(name = "data")
    override val data: Multi,

    ) : Envelope<Multi>
