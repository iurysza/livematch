package dev.iurysouza.livematch.footballinfo.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Pagination(

  val items: Int,
  val page: Int,
  val perPage: Int,
)
