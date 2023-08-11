package dev.iurysouza.livematch.matchthread

import dev.iurysouza.livematch.matchthread.models.MatchStatus
import dev.iurysouza.livematch.matchthread.models.Score
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class MatchEventParserTest : BehaviorSpec(
  {
    //kotest test for parse content method
    given("2 to 0 score, one player scoring multiple twice") {
      val content =
        "\n*LAFC scorers: Denis Bouanga 52', 56', Nathan Ordaz 62', Filip Yavorov Krastev 84'*\n\n\n\n"

      `when`("parse content is called") {
        val result = MatchEventParser().parseContent(content)
        then("it should return a valid MatchStatus object") {
          result shouldBe MatchStatus(
            homeScore = listOf(
              Score(minute = "52", player = "Denis Bouanga"),
              Score(minute = "56", player = "Denis Bouanga"),
              Score(minute = "62", player = "Nathan Ordaz"),
              Score(minute = "84", player = "Filip Yavorov Krastev"),
            ),
            awayScore = listOf(),
            description = "",
          )
        }
      }
    }
    given("another test") {
      val content =
        "*Manchester City scorers: Erling Haaland (4', 36')*"

      `when`("parse content is called") {
        val result = MatchEventParser().parseContent(content)
        then("it should return a valid MatchStatus object") {
          result shouldBe MatchStatus(
            homeScore = listOf(
              Score(minute = "70", player = "Gabriel Mercado"),
              Score(minute = "78", player = "Alan Patrick"),
            ),
            awayScore = listOf(
              Score(minute = "90", player = "Robert Rojas"),
            ),
            description = "",
          )
        }
      }
    }
    given("1 to 1 score, each player scoring once") {
      val content =
        "\n*Internacional scorers: Gabriel Mercado 70', Alan Patrick 78'*\n\n*River Plate scorers: Robert Rojas 90'*\n\n"

      `when`("parse content is called") {
        val result = MatchEventParser().parseContent(content)
        then("it should return a valid MatchStatus object") {
          result shouldBe MatchStatus(
            homeScore = listOf(
              Score(minute = "70", player = "Gabriel Mercado"),
              Score(minute = "78", player = "Alan Patrick"),
            ),
            awayScore = listOf(
              Score(minute = "90", player = "Robert Rojas"),
            ),
            description = "",
          )
        }
      }
    }
  },
)

