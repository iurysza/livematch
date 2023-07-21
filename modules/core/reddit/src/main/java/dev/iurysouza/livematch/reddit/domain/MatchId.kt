package dev.iurysouza.livematch.reddit.domain

@JvmInline
value class MatchTitle(val value: String) {
  init {
    require(value.isNotEmpty()) { "MatchTitle must not be empty" }
  }
}

@JvmInline
value class MatchId(val value: String) {
  init {
    require(value.isNotEmpty()) { "MatchId must not be empty" }
  }
}
