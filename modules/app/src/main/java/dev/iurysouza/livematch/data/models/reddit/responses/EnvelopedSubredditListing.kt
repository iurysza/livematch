package dev.iurysouza.livematch.data.models.reddit.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.data.models.reddit.responses.base.Envelope
import dev.iurysouza.livematch.data.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.data.models.reddit.responses.listings.SubredditListing

@JsonClass(generateAdapter = true)
class EnvelopedSubredditListing(

    @Json(name = "kind")
    override val kind: EnvelopeKind,

    @Json(name = "data")
    override val data: SubredditListing,

    ) : Envelope<SubredditListing>