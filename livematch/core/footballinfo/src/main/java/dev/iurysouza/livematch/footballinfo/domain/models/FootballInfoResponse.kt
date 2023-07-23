package dev.iurysouza.livematch.footballinfo.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FootballInfoResponse(

  val errors: List<Any>,
  val pagination: List<Pagination>,
  val result: List<Match>,
  val status: Int,
)
