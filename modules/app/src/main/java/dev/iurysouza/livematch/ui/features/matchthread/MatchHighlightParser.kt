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
            media.title!!.contains(homeTeam) && media.title.contains(awayTeam)
        }.map { media ->
            MediaItem(
                titleByteArray = parseTitle(media),
                urlByteArray = media.html!!.toByteArray(),
            )
        }
    }.mapLeft { ViewError.MatchMediaParsingError(it.message.toString()) }

    private fun parseTitle(media: MatchHighlight): ByteArray {
        if (media.title!!.contains("href")) {
            val title = media.title.substringAfter("href=\"").substringBefore("\">")
            return title.toByteArray()
        }
        return media.title.toByteArray()
    }

    private fun String.parseTeamNames(): Pair<String, String> {
        val regexMatch = HOME_VS_AWAY.toRegex().find(this)!!
        val (homeTeam, awayTeam) = regexMatch.destructured
        return homeTeam to awayTeam
    }
}

private const val HOME_VS_AWAY = """(.*) vs(\.)? (.*)"""
