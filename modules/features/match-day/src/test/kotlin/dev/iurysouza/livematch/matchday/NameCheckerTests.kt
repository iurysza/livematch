package dev.iurysouza.livematch.matchday

import dev.iurysouza.livematch.matchday.models.NameChecker
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class NameCheckerTests : FunSpec(
  {
    val twoWordedHomeTeam = "Manchester City"
    val awayTeam = "Liverpool"
    val twoWordedTeamTitle = "Manchester United 1-0 Liverpool"
    test("Should return false if home team in title does not match actual team") {
      NameChecker.isMatchRelated(
        homeTeam = twoWordedHomeTeam,
        awayTeam = awayTeam,
        title = twoWordedTeamTitle,
      ) shouldBe false
    }

    test("Should return false if none of the teams in title match with provided home or away team") {
      NameChecker.isMatchRelated(
        homeTeam = twoWordedHomeTeam,
        awayTeam = awayTeam,
        title = "Manchester United 1-0 Chelsea",
      ) shouldBe false
    }

    test("Should return true if both teams in title match with provided home and away teams") {
      NameChecker.isMatchRelated(
        homeTeam = "PSG",
        awayTeam = "Manchester City",
        title = "Paris Saint-Germain 1-0 Manchester City",
      ) shouldBe true
    }

  },
)
