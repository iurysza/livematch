package dev.iurysouza.livematch.reddit.domain

import arrow.core.Either
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.common.MappingError
import dev.iurysouza.livematch.reddit.domain.models.MatchHighlightEntity
import dev.iurysouza.livematch.reddit.domain.models.MediaEntity

class MatchHighlightParserUseCase {

  fun getMatchHighlights(
      matchMedias: List<MatchHighlightEntity>,
      matchParams: MatchParams,
  ): Either<DomainError, List<MediaEntity>> = Either.catch {
    matchMedias
      .sortedBy { it.createdAt }
      .filter { media ->
        isMediaRelatedToTeams(
          media.title,
          matchParams.homeTeam,
          matchParams.awayTeam,
        )
      }
      .map {
        MediaEntity(
          title = parseTitle(it),
          url = it.html!!,
          id = it.parentId,
        )
      }
  }.mapLeft { MappingError(it.message.toString()) }

  private fun isMediaRelatedToTeams(
    mediaTitle: String?,
    homeTeam: String,
    awayTeam: String,
  ): Boolean {
    return (NameChecker.containsTeamName(mediaTitle!!, homeTeam) ||
      NameChecker.containsTeamName(mediaTitle, awayTeam)) &&
      // reject U21, U17, U19, etc
      !mediaTitle.contains("U\\d+".toRegex())
  }

  private fun parseTitle(media: MatchHighlightEntity): String {
    if (media.title!!.contains("href")) {
      return media.title.substringAfter("href=\"").substringBefore("\">")
    }
    return media.title
  }

  private fun containsTeamName(title: String, teamName: String): Boolean {
    val nickName: String = teamNickNameDictionary.getOrElse(teamName) { "_invalid_" }
    val teamNameWords = teamName.split(" ")
    val longestWordInTeamName = teamNameWords.maxBy { it.length }
    return title.contains(teamName) ||
      title.contains(nickName) ||
      title.contains(longestWordInTeamName)
  }

  private fun String.parseTeamNames(): Pair<String, String> {
//        val regexMatch = HOME_VS_AWAY.toRegex().find(this)!!
//        val (homeTeam, awayTeam) = regexMatch.destructured
    val pattern = "\\W+".toRegex()
    val words = pattern.split(this).filter { it.isNotBlank() }
    val new = words.takeWhile { it != "vs" }
    val homeTeam = new.drop(2)

    val awayTeam = words.takeLastWhile { it != "vs" }.takeWhile { it != "FIFA" }

    return homeTeam.joinToString(" ") to awayTeam.joinToString(" ")
  }

  private val teamNickNameDictionary = mapOf(
    "Paris Saint-Germain" to "PSG",
  )
}

object NameChecker {
  private val ambiguousWords = listOf(
    "Borussia",
    "United",
    "City",
    "Real",
    "Arsenal",
  )
  private val teamNickNameDictionary = mapOf(
    "Paris Saint-Germain" to "PSG",
    "Manchester United" to "Manchester Utd",
  )

  private val reverseDictionary = teamNickNameDictionary.entries.associate { (key, value) ->
    value to key
  }


  /**
   * Checks if the given [title] contains a team name.
   *
   * @param title The title to check for the presence of a team name.
   * @param teamName The team name to search for in the [title].
   * @return `true` if the [title] contains any variations of the [teamName], `false` otherwise.
   */
  fun containsTeamName(title: String, teamName: String): Boolean {
    val wordsSortedByLength = teamName.split(" ").sortedBy { it.length }.reversed()
    val wordsToCheck = buildList {
      add(teamName)
      reverseDictionary[teamName]?.let { add(it) }
      teamNickNameDictionary[teamName]?.let { add(it) }
      wordsSortedByLength.firstOrNull()?.let { maxLengthWord ->
        if (!ambiguousWords.contains(maxLengthWord)) {
          add(maxLengthWord)
        } else {
          wordsSortedByLength.getOrNull(1)?.let { add(it) }
        }
      }
    }
    return wordsToCheck.any { title.contains(it) }
  }

  fun isMatchRelated(
    homeTeam: String,
    awayTeam: String,
    title: String,
  ): Boolean {
    return containsTeamName(title, homeTeam) && containsTeamName(title, awayTeam)
  }
}
