package dev.iurysouza.livematch.footballinfo.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DominanceIndex(

  val teamA: String,
  val teamB: String,
  val timer: String,
)
