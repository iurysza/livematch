package dev.iurysouza.livematch.footballinfo.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Shoots(
  val gA: String?,
  val off: String,
  val on: String,
  val t: String,
)
