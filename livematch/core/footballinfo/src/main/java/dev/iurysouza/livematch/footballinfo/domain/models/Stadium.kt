package dev.iurysouza.livematch.footballinfo.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Stadium(

  val id: String,
  val name: String,
)
