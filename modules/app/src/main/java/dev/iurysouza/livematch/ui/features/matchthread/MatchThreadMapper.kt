package dev.iurysouza.livematch.ui.features.matchthread

import android.util.Log
import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.continuations.either
import arrow.core.flatMap
import dev.iurysouza.livematch.domain.adapters.models.CommentsEntity
import dev.iurysouza.livematch.domain.adapters.models.MatchHighlight
import dev.iurysouza.livematch.domain.adapters.models.MatchThreadEntity
import dev.iurysouza.livematch.ui.features.matchlist.MatchItem
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


open class MatchThreadMapper {

    suspend fun toMatchThread(
        matchItem: MatchItem,
        highlights: List<MatchHighlight>,
        lastMatches: List<MatchThreadEntity>,
    ): Either<ViewError, MatchThread> = catch {
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

    fun getMatchEvents(content: String): Pair<List<MatchEvent>, ByteArray> {

        val finalContent = content
            .substringBefore("[Auto-refreshing")
            .replace("#**", "## **")
            .replace("(#bar-3-white)", "")
            .replace("]", "")
            .replace("[", "")


        val matchEventList = content
            .substringAfter("*via [ESPN]")
            .substringBefore("--------")

        val eventList =
            runCatching { parseEventList(matchEventList) }.getOrNull() ?: let {
                parseChampionsLeagueEvents(matchEventList)
            }
        return buildList {
            add(
                MatchEvent(
                    relativeTime = "1",
                    icon = EventIcon.KickOff,
                    description = "Match Started",
                    keyEvent = true
                )
            )
            addAll(eventList)
            add(
                MatchEvent(
                    relativeTime = "1020",
                    icon = EventIcon.FinalWhistle,
                    description = "Match Ended",
                    keyEvent = true
                )
            )
        } to finalContent.toByteArray()
    }


    private fun parseChampionsLeagueEvents(text: String): List<MatchEvent> =
        text.split("\n").mapNotNull { input ->
            runCatching {
                val icon = Regex(ICONS_PATTERN).findAll(input).first().value
                val time = Regex(TIME_PATTERN).findAll(input).first().value
                val description = input
                    .remove(time)
                    .remove(icon)
                    .trim()

                val (isKeyEvent, finalDescription) = parseDescription(description)
                MatchEvent(
                    relativeTime = time.replace("'", ""),
                    icon = EventIcon.fromString(icon.substringAfter("(").substringBefore(")")),
                    description = finalDescription,
                    keyEvent = isKeyEvent
                )
            }.getOrNull()
        }

    private fun parseDescription(description: String): Pair<Boolean, String> {
        var isKeyEvent = false
        val finalDescription = if (description.contains("**")) {
            isKeyEvent = true
            description.substringAfter("**").substringBefore("**")
        } else {
            isKeyEvent = false
            description
        }
        return Pair(isKeyEvent, finalDescription)
    }

    fun String.remove(regex: String): String {
        return replace(regex, "")
    }

    private fun parseEventList(matchEventList: String) = matchEventList
        .split("\n")
        .drop(1)
        .filterNot { it == "" }
        .map {
            // This code parses a match event mardkown line into our MatchEvent object
            // a raw match event string looks like this:
            // `"**40'** [](#icon-yellow) Nicolás De La Cruz (River Plate) is shown the yellow card for a bad foul."`

            // "40"
            // "icon-yellow"
            // "Nicolás De La Cruz (River Plate) is shown the yellow card for a bad foul."

            val (time, description) = it.split("[")
            val (isKeyEvent, finalDescription) = parseDescription(description)
            MatchEvent(
                relativeTime = time.replace("*", "").replace("'", "").trim(),
                icon = EventIcon.fromString(description.substringAfter("#").substringBefore(")")),
                description = finalDescription.substringAfter(")").trim(),
                keyEvent = isKeyEvent
            )
        }

    private fun getMatchHighlights(
        matchMedias: List<MatchHighlight>,
        matchTitle: String,
    ): Either<ViewError.MatchMediaParsingError, List<MediaItem>> = catch {
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

    fun toCommentSectionListEvents(
        commentList: List<CommentItem>,
        eventList: List<MatchEvent>,
    ) = catch {
        runCatching {
            val eventStack = ArrayDeque<MatchEvent>()
            eventList.forEach { eventStack.add(it) }

            val commentStack = ArrayDeque<CommentItem>()
            commentList.forEach { commentStack.add(it) }

            val commentSectionList = mutableListOf<CommentSection>()

            while (eventStack.isNotEmpty() && commentStack.isNotEmpty()) {
                var lastEventTime = eventStack.last().relativeTime()
                var lastCommentTime = commentStack.last().relativeTime

                val sectionComments = mutableListOf<CommentItem>()

                while (lastEventTime <= lastCommentTime && commentStack.isNotEmpty()) {
                    sectionComments.add(commentStack.last())
                    commentStack.removeLast()
                    lastCommentTime = commentStack.last().relativeTime
                    lastEventTime = eventStack.last().relativeTime()
                }
                commentSectionList.add(
                    CommentSection(
                        lastEventTime.toString(),
                        event = eventStack.last(),
                        commentList = sectionComments.reversed(),
                    )
                )
                eventStack.removeLast()
            }

            commentSectionList.toList().reversed()
        }.onFailure {
            Log.e("MatchThreadMapper", it.message.toString())
        }.getOrNull()!!
    }

    fun toCommentItemList(
        commentList: List<CommentsEntity>,
        matchStartTime: Long,
    ): Either<Throwable, List<CommentItem>> = catch {
        commentList.map { comment ->
            CommentItem(
                author = comment.author,
                body = comment.body,
                relativeTime = calculateRelativeTime(comment.created, matchStartTime),
                score = comment.score.toString(),
            )
        }.sortedBy { it.relativeTime }
    }
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

fun List<MatchThreadEntity>.toMatchItem(
    enabledCompetitions: List<String>,
): List<MatchItem> = mapNotNull { match ->
    val (title, subtitle) = parseTitle(match.title) ?: return@mapNotNull null
    MatchItem(match.id, title, subtitle.replace("]", ""))
}
    .filter { enabledCompetitions.any { competition -> it.competition.contains(competition) } }
    .sortedBy { it.competition }

private const val TIME_PATTERN = """((\d)*')"""
private const val ICONS_PATTERN = """\[([^\[\]]*?)]\((\S*?)\)"""

private fun MatchEvent.relativeTime(): Int = runCatching {
    relativeTime.toInt()
}.getOrNull() ?: run {
    // handles scenarios where event time is like: 45+1, 90+7
    val (fullTime, overtime) = relativeTime.split("+")
    fullTime.toInt() + overtime.toInt()
}

private fun calculateRelativeTime(
    commentTime: Long,
    matchTime: Long,
): Int = Duration.between(
    matchTime.toUTCLocalDateTime(),
    commentTime.toUTCLocalDateTime(),
).toMinutes().toInt()

private fun Long.toUTCLocalDateTime() =
    LocalDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.systemDefault())
