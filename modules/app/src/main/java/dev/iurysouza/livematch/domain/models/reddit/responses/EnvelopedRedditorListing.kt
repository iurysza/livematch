package dev.iurysouza.livematch.domain.models.reddit.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.domain.models.reddit.responses.base.Envelope
import dev.iurysouza.livematch.domain.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.domain.models.reddit.responses.listings.RedditorListing

@JsonClass(generateAdapter = true)
class EnvelopedRedditorListing(

    @Json(name = "kind")
    override val kind: EnvelopeKind,

    @Json(name = "data")
    override val data: RedditorListing,

    ) : Envelope<RedditorListing>
