package dev.iurysouza.livematch.footballinfo.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Fouls(
  val rC: String?,
  val t: String?,
  val yC: String?,
  val yTRC: String?,
)
