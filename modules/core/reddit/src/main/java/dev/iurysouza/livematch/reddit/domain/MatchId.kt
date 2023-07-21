package dev.iurysouza.livematch.reddit.domain

@JvmInline
value class MatchId(val value: String) {
  init {
    require(value.isNotEmpty()) { "MatchId must not be empty" }
  }
}
