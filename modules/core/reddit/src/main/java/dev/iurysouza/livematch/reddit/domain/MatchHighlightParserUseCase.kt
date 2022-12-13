package dev.iurysouza.livematch.reddit.domain

import arrow.core.Either
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.common.MappingError
import dev.iurysouza.livematch.reddit.domain.models.MatchHighlightEntity
import dev.iurysouza.livematch.reddit.domain.models.MediaEntity

class MatchHighlightParserUseCase {

  fun getMatchHighlights(
    matchMedias: List<MatchHighlightEntity>,
    matchTitle: String,
  ): Either<DomainError, List<MediaEntity>> = Either.catch {
    val (homeTeam, awayTeam) = matchTitle.parseTeamNames()
    matchMedias.sortedBy { it.createdAt }.filter { media ->
      containsTeamName(media.title!!, homeTeam) || containsTeamName(media.title, awayTeam)
    }.map { media ->
      MediaEntity(
        title = parseTitle(media),
        url = media.html!!,
      )
    }
  }.mapLeft { MappingError(it.message.toString()) }

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

private const val HOME_VS_AWAY = """Match Thread: (.*) vs (.*)(\| | \[)* (.*)"""
