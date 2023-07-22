package dev.iurysouza.livematch.footballinfo.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Event(

  val team: String,
  val timer: String,
  val type: String,
)
