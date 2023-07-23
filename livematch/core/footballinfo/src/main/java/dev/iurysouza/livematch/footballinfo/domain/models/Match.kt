package dev.iurysouza.livematch.footballinfo.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Match(
  val bet365Url: String?,
  val championship: Championship,
  val date: String,
  val dominanceIndex: List<DominanceIndex>?,
  val events: List<Event>?,
  val id: String,
  val referee: Referee?,
  val stadium: Stadium?,
  val status: String,
  val teamA: TeamA,
  val teamB: TeamB,
  val timer: String,
)
