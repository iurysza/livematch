package dev.iurysouza.livematch.matchday.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random

@Suppress("MagicNumber")
object Fakes {
  private const val DEFAULT = 10
  fun generateMatchList(count: Int = DEFAULT): ImmutableList<MatchUiModel> {
    val list = mutableListOf<MatchUiModel>()
    repeat(count / 2) {
      if (it % 2 == 0)
        list.add(generateMatch(it))
    }
    repeat(count / 2) {
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
        randomWordWithMaxChars(15),
        isHomeTeam = true,
        isAhead = homeScore > awayScore,
        score = "$homeScore",
      ),
      awayTeam = Team(
        crestUrl = "https://crests.football-data.org/770.svg",
        randomWordWithMaxChars(15),
        isHomeTeam = false,
        isAhead = homeScore > awayScore,
        score = "$awayScore",
      ),
      startTime = "${(14..20).random()}:00",
      elapsedMinutes = "${(0..90).random()}",
      competition = competition(randomWordWithMaxChars(10)),
    )
  }

  private fun randomWordWithMaxChars(max: Int): String =
    buildString {
      repeat(Random.nextInt(5, max)) {
        append(('a'..'z').random())
      }
    }
}

private fun competition(name: String, id: Int = Random.nextInt()) = Competition(
  name = name,
  id = id,
  emblemUrl = "https://crests.football-data.org/770.svg",
)

fun IntRange.random() = Random.nextInt((endInclusive + 1) - start) + start
