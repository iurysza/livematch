package dev.iurysouza.livematch.data.models.cloned.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.data.models.cloned.responses.base.Envelope
import dev.iurysouza.livematch.data.models.cloned.responses.base.EnvelopeKind
import dev.iurysouza.livematch.data.models.cloned.models.WikiPage

@JsonClass(generateAdapter = true)
class EnvelopedWikiPage(

    @Json(name = "kind")
    override val kind: EnvelopeKind?,

    @Json(name = "data")
    override val data: WikiPage?,

    @Json(name = "reason")
    val reason: String? = null,

    @Json(name = "message")
    val message: String? = null,

    ) : Envelope<WikiPage>
