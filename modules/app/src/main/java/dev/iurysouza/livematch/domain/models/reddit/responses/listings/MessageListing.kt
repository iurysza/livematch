package dev.iurysouza.livematch.domain.models.reddit.responses.listings

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedMessage
import dev.iurysouza.livematch.domain.models.reddit.responses.base.Listing

@JsonClass(generateAdapter = true)
class MessageListing(

    @Json(name = "modhash")
    override val modhash: String?,
    @Json(name = "dist")
    override val dist: Int?,

    @Json(name = "children")
    override val children: List<EnvelopedMessage>,

    @Json(name = "after")
    override val after: String?,
    @Json(name = "before")
    override val before: String?,

    ) : Listing<EnvelopedMessage>