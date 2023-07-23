package dev.iurysouza.livematch.footballinfo.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StatsX(

  val attacks: Attacks,
  val corners: Corners,
  val dominanceAvg25: String?,
  val fouls: Fouls,
  val injuries: String?,
  val penalties: String,
  val possession: String?,
  val shoots: Shoots,
  val substitutions: String?,
  val throwins: Any?,
)
