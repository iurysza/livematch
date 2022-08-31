package dev.iurysouza.livematch.domain.models.reddit.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.domain.models.reddit.responses.base.Envelope
import dev.iurysouza.livematch.domain.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.domain.models.reddit.responses.listings.RedditorDataListing

@JsonClass(generateAdapter = true)
class EnvelopedRedditorDataListing(

    @Json(name = "kind")
    override val kind: EnvelopeKind,

    @Json(name = "data")
    override val data: RedditorDataListing,

    ) : Envelope<RedditorDataListing>
