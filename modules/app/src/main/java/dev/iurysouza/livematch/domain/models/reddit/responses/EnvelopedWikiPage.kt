package dev.iurysouza.livematch.domain.models.reddit.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.domain.models.reddit.responses.base.Envelope
import dev.iurysouza.livematch.domain.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.domain.models.reddit.entities.WikiPage

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
