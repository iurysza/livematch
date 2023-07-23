package dev.iurysouza.livematch.reddit.data.models.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.reddit.data.models.entities.base.SubredditData
import dev.iurysouza.livematch.reddit.data.models.responses.base.Envelope
import dev.iurysouza.livematch.reddit.data.models.responses.base.EnvelopeKind

@JsonClass(generateAdapter = true)
class EnvelopedSubredditData(

  @Json(name = "kind")
  override val kind: EnvelopeKind,

  @Json(name = "data")
  override val data: SubredditData,

) : Envelope<SubredditData>, EnvelopedData
