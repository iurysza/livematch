package dev.iurysouza.livematch.matchday.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random

@Suppress("MagicNumber")
object Fakes {
  fun generateMatchList(count: Int): ImmutableList<MatchUiModel> {
    val list = mutableListOf<MatchUiModel>()
    repeat(count) {
      list.add(generateMatch(it))
    }
    return list.toImmutableList()
  }

  private fun generateMatch(seed: Int): MatchUiModel {
    val homeScore = (0..5).random()
    val awayScore = (0..5).random()
    return MatchUiModel(
      id = seed.toString(),
      homeTeam = Team(
        crestUrl = "https://crests.football-data.org/770.svg",
        name = "England",
        isHomeTeam = true,
        isAhead = homeScore > awayScore,
        score = "$homeScore",
      ),
      awayTeam = Team(
        crestUrl = "https://crests.football-data.org/770.svg",
        name = "England",
        isHomeTeam = false,
        isAhead = homeScore > awayScore,
        score = "$awayScore",
      ),
      startTime = "${(14..20).random()}:00",
      elapsedMinutes = "${(0..90).random()}",
      competition = Competition(
        name = "Euro 2020",
        id = 1,
        emblemUrl = "https://crests.football-data.org/770.svg",
      ),
    )
  }
}

fun IntRange.random() = Random.nextInt((endInclusive + 1) - start) + start
