package dev.iurysouza.livematch.data.models.reddit.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.data.models.reddit.responses.base.Envelope
import dev.iurysouza.livematch.data.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.data.models.reddit.responses.listings.ContributionListing

@JsonClass(generateAdapter = true)
class EnvelopedContributionListing(

    @Json(name = "kind")
    override val kind: EnvelopeKind,

    @Json(name = "data")
    override val data: ContributionListing,

    ) : Envelope<ContributionListing>
