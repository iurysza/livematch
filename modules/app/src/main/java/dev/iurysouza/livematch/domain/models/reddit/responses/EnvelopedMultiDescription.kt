package dev.iurysouza.livematch.domain.models.reddit.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.domain.models.reddit.entities.MultiDescription
import dev.iurysouza.livematch.domain.models.reddit.responses.base.Envelope
import dev.iurysouza.livematch.domain.models.reddit.responses.base.EnvelopeKind

@JsonClass(generateAdapter = true)
class EnvelopedMultiDescription(

    @Json(name = "kind")
    override val kind: EnvelopeKind,

    @Json(name = "data")
    override val data: MultiDescription,

    ) : Envelope<MultiDescription>
