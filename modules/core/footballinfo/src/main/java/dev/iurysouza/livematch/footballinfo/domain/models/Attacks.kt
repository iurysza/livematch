package dev.iurysouza.livematch.footballinfo.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Attacks(
  val d: String,
  val n: String,
  val oS: String?,
)
