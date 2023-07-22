package dev.iurysouza.livematch.footballinfo.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Score(

  val f: String?,
  val h1: String?,
  val h2: String?,
  val o: Any?,
  val p: Any?,
)
