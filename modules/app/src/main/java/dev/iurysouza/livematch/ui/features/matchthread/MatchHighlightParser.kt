package dev.iurysouza.livematch.ui.features.matchthread

import arrow.core.Either
import dev.iurysouza.livematch.domain.adapters.models.MatchHighlight


open class MatchHighlightParser {

    fun getMatchHighlights(
        matchMedias: List<MatchHighlight>,
        matchTitle: String,
    ): Either<ViewError.MatchMediaParsingError, List<MediaItem>> = Either.catch {
        val (homeTeam, awayTeam) = matchTitle.parseTeamNames()
        matchMedias.filter { media ->
            containsTeamName(media.title!!, homeTeam) && containsTeamName(media.title, awayTeam)
        }.map { media ->
            MediaItem(
                title = parseTitle(media),
                url = media.html!!,
            )
        }
    }.mapLeft { ViewError.MatchMediaParsingError(it.message.toString()) }

    private fun parseTitle(media: MatchHighlight): String {
        if (media.title!!.contains("href")) {
            return media.title.substringAfter("href=\"").substringBefore("\">")
        }
        return media.title
    }

    private fun containsTeamName(title: String, teamName: String): Boolean {
        val nickName: String = teamNickNameDictionary.getOrElse(teamName) { "_invalid_" }
        val teamNameWords = teamName.split(" ")
        val longestWordInTeamName = teamNameWords.maxBy { it.length }
        return title.contains(teamName)
                || title.contains(nickName)
                || title.contains(longestWordInTeamName)
    }

    private fun String.parseTeamNames(): Pair<String, String> {
        val regexMatch = HOME_VS_AWAY.toRegex().find(this)!!
        val (homeTeam, awayTeam) = regexMatch.destructured
        return homeTeam to awayTeam
    }

    private val teamNickNameDictionary = mapOf<String, String>(
        "Paris Saint-Germain" to "PSG"
    )
}

private const val HOME_VS_AWAY = """(.*) vs(\.)? (.*)"""
