package dev.iurysouza.livematch.footballinfo.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Corners(

  val f: String,
  val h: String,
  val t: String,
)
