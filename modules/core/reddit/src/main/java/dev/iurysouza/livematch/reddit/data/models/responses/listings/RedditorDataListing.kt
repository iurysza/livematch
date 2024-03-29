package dev.iurysouza.livematch.reddit.data.models.responses.listings

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.reddit.data.models.responses.EnvelopedRedditorData
import dev.iurysouza.livematch.reddit.data.models.responses.base.Listing

@JsonClass(generateAdapter = true)
class RedditorDataListing(

  @Json(name = "modhash")
  override val modhash: String?,
  @Json(name = "dist")
  override val dist: Int?,

  @Json(name = "children")
  override val children: List<EnvelopedRedditorData>,

  @Json(name = "after")
  override val after: String?,
  @Json(name = "before")
  override val before: String?,

) : Listing<EnvelopedRedditorData>
