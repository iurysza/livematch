package dev.iurysouza.livematch.reddit.data.models.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.reddit.data.models.entities.WikiPage
import dev.iurysouza.livematch.reddit.data.models.responses.base.Envelope
import dev.iurysouza.livematch.reddit.data.models.responses.base.EnvelopeKind

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
