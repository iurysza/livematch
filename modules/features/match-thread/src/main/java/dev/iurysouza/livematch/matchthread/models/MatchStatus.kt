package dev.iurysouza.livematch.matchthread.models

data class MatchStatus(
  val homeScore: List<Score>,
  val awayScore: List<Score>,
  val description: String = "",
)

data class Score(
  val minute: String,
  val player: String,
)
