package dev.iurysouza.livematch.reddit.data.models.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.reddit.data.models.responses.base.Envelope
import dev.iurysouza.livematch.reddit.data.models.responses.base.EnvelopeKind
import dev.iurysouza.livematch.reddit.data.models.responses.listings.SubredditDataListing

@JsonClass(generateAdapter = true)
class EnvelopedSubredditDataListing(

  @Json(name = "kind")
  override val kind: EnvelopeKind,

  @Json(name = "data")
  override val data: SubredditDataListing,

) : Envelope<SubredditDataListing>
