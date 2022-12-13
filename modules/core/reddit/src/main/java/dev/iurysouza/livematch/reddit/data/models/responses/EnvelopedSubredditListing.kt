package dev.iurysouza.livematch.reddit.data.models.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.reddit.data.models.responses.base.Envelope
import dev.iurysouza.livematch.reddit.data.models.responses.base.EnvelopeKind
import dev.iurysouza.livematch.reddit.data.models.responses.listings.SubredditListing

@JsonClass(generateAdapter = true)
class EnvelopedSubredditListing(

  @Json(name = "kind")
  override val kind: EnvelopeKind,

  @Json(name = "data")
  override val data: SubredditListing,

) : Envelope<SubredditListing>
