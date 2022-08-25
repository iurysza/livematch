package dev.iurysouza.livematch.data.models.cloned.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.data.models.cloned.responses.base.Envelope
import dev.iurysouza.livematch.data.models.cloned.responses.base.EnvelopeKind
import dev.iurysouza.livematch.data.models.cloned.models.Trophy

@JsonClass(generateAdapter = true)
class EnvelopedTrophy(

    @Json(name = "kind")
    override val kind: EnvelopeKind,

    @Json(name = "data")
    override val data: Trophy,

    ) : Envelope<Trophy>