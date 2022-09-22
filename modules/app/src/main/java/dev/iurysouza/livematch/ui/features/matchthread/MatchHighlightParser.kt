package dev.iurysouza.livematch.ui.features.matchthread

import android.util.Log
import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.flatMap
import dev.iurysouza.livematch.domain.adapters.models.MatchHighlight
import dev.iurysouza.livematch.domain.adapters.models.MatchThreadEntity
import dev.iurysouza.livematch.ui.features.matchlist.MatchItem


open class MatchHighlightParser {

    suspend fun toMatchThread(
        matchItem: MatchItem,
        highlights: List<MatchHighlight>,
        lastMatches: List<MatchThreadEntity>,
    ): Either<ViewError, MatchThread> = Either.catch {
        val matchEntity = lastMatches.find { it.id == matchItem.id }!!
        matchEntity to matchItem
    }.mapLeft {
        ViewError.InvalidMatchId(it.message.toString())
    }.flatMap { (matchEntity, matchItem) ->
        either<ViewError, MatchThread> {
            MatchThread(
                id = matchEntity.id,
                title = matchItem.title,
                competition = matchItem.competition,
                contentByteArray = matchEntity.content.toByteArray(),
                startTime = matchEntity.createdAt,
                mediaList = getMatchHighlights(highlights, matchItem.title).bind()
            )
        }
    }


    private fun getMatchHighlights(
        matchMedias: List<MatchHighlight>,
        matchTitle: String,
    ): Either<ViewError.MatchMediaParsingError, List<MediaItem>> = Either.catch {
        val (teamA, teamB) = matchTitle.parseTeamNames()
        matchMedias.filter { media ->
            media.title!!.contains(teamA) && media.title.contains(teamB)
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

    private fun String?.parseTeamNames(): List<String> = this
        ?.split("vs")
        ?.map { it.trim().split(" ").first() }
        ?: emptyList()
}


fun parseTitle(matchTitle: String): Pair<String, String>? = runCatching {
    val (title, subtitle) = matchTitle
        .replace("Match Thread:", "")
        .split("|")
    title to subtitle
}.onFailure { Log.e("LiveMatch", "Error parsing match thread: ${matchTitle})", it) }
    .getOrNull() ?: runCatching {
    val (title, subtitle) = matchTitle
        .replace("Match Thread:", "")
        .split("[")
    title to subtitle.replace("]", "")
}.getOrNull()
