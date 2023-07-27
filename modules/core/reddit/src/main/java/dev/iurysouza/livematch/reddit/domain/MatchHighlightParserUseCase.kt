package dev.iurysouza.livematch.reddit.domain

import arrow.core.Either
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.common.MappingError
import dev.iurysouza.livematch.reddit.domain.models.MatchHighlightEntity
import dev.iurysouza.livematch.reddit.domain.models.MediaEntity

class MatchHighlightParserUseCase {

  fun getMatchHighlights(
    matchMedias: List<MatchHighlightEntity>,
    matchTitle: MatchTitle,
  ): Either<DomainError, List<MediaEntity>> = Either.catch {
    matchMedias
      .sortedBy { it.createdAt }
      .filter { media -> isMediaRelatedToTeams(media, matchTitle) }
      .map {
        MediaEntity(
          title = parseTitle(it),
          url = it.html!!,
        )
      }
  }.mapLeft { MappingError(it.message.toString()) }

  private fun isMediaRelatedToTeams(
    media: MatchHighlightEntity,
    matchTitle: MatchTitle,
  ): Boolean = containsTeamName(media.title ?: "", matchTitle.homeTeam)||
    containsTeamName(media.title ?: "", matchTitle.awayTeam)

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
