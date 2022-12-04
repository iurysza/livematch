package dev.iurysouza.livematch.reddit.data.models.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.reddit.data.models.responses.base.Envelope
import dev.iurysouza.livematch.reddit.data.models.responses.base.EnvelopeKind
import dev.iurysouza.livematch.reddit.data.models.responses.listings.MessageListing

@JsonClass(generateAdapter = true)
class EnvelopedMessageListing(

    @Json(name = "kind")
    override val kind: EnvelopeKind,

    @Json(name = "data")
    override val data: MessageListing,

    ) : Envelope<MessageListing>
