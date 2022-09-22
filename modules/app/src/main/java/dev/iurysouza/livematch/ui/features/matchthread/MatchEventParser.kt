package dev.iurysouza.livematch.ui.features.matchthread

import android.util.Log
import arrow.core.Either
import arrow.core.Either.Companion.catch
import dev.iurysouza.livematch.domain.adapters.models.CommentsEntity
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


open class MatchEventParser {

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

        val eventList = runCatching { parseEventList(matchEventList) }.getOrNull()
            ?: parseChampionsLeagueEvents(matchEventList)
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
                    relativeTime = "300",
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

    private fun String.remove(regex: String): String = replace(regex, "")

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

            val sectionStack = ArrayDeque<CommentSection>()
            val reversed = commentSectionList.toList().reversed()

            reversed.forEach { sectionStack.add(it) }
            buildList {
                while (sectionStack.isNotEmpty()) {
                    val lastSection = sectionStack.last()
                    sectionStack.removeLast()
                    add(lastSection.copy(commentList = sectionStack.lastOrNull()?.commentList?.reversed()
                        ?: emptyList()))
                }
            }
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

    private fun calculateRelativeTime(
        commentTime: Long,
        matchTime: Long,
    ): Int = Duration.between(
        matchTime.toUTCLocalDateTime(),
        commentTime.toUTCLocalDateTime(),
    ).toMinutes().toInt()

    private fun Long.toUTCLocalDateTime() =
        LocalDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.systemDefault())

    private fun MatchEvent.relativeTime(): Int = runCatching {
        relativeTime.toInt()
    }.getOrNull() ?: run {
        // handles scenarios where event time is like: 45+1, 90+7
        val (fullTime, overtime) = relativeTime.split("+")
        fullTime.toInt() + overtime.toInt()
    }

}

private const val TIME_PATTERN = """((\d)*')"""
private const val ICONS_PATTERN = """\[([^\[\]]*?)]\((\S*?)\)"""

