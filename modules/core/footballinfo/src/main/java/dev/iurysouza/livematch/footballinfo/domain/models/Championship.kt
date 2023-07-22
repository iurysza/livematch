package dev.iurysouza.livematch.footballinfo.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Championship(

  val id: String,
  val name: String,
  val sName: String?,
)
