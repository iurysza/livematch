package dev.iurysouza.livematch.reddit.domain

data class MatchParams(
  val pages: Int,
  val homeTeam: String,
  val awayTeam: String,
)

@JvmInline
value class MatchId(val value: String) {
  init {
    require(value.isNotEmpty()) { "MatchId must not be empty" }
  }
}
