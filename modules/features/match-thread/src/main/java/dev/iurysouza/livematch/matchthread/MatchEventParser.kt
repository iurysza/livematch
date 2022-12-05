package dev.iurysouza.livematch.matchthread

import arrow.core.Either
import arrow.core.Either.Companion.catch
import dev.iurysouza.livematch.reddit.domain.models.CommentsEntity
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import timber.log.Timber


open class MatchEventParser {

    fun getMatchEvents(content: String): Pair<List<MatchEvent>, String> {
        val contentList = content.split("\n")
        var headline = contentList.first()
        val scoreValue = headline.substringAfter("[").substringBefore("]")
        runCatching {
            headline = headline.replace(
                Regex(ICONS_PATTERN).findAll(headline).first().value,
                scoreValue
            ).replace("#**", "# **")
        }

        val finalContent = contentList
            .drop(1)
            .joinToString("\n")
            .substringBefore("-----\n")
            .replace("---", "")

        val matchEventList = contentList.takeLastWhile {
            !it.contains("**MATCH EVENTS**", true)
        }.joinToString("\n")

        val eventList = runCatching {
            parseChampionsLeagueEvents(matchEventList).dropLast(1)
        }.getOrElse { emptyList() }

        return buildList {
            add(
                MatchEvent(
                    relativeTime = "0",
                    icon = EventIcon.KickOff,
                    description = "Kick Off!\n",
                    keyEvent = false
                )
            )
            addAll(eventList)
            add(
                MatchEvent(
                    relativeTime = "300",
                    icon = EventIcon.FinalWhistle,
                    description = "Last Comments\n",
                    keyEvent = false
                )
            )
        } to finalContent
    }

    /**
     * This code parses a match event mardkown line into our MatchEvent object
     * a raw match event string looks like this:
     * `"**40'** [](#icon-yellow) Nicolás De La Cruz (River Plate) is shown the yellow card for a bad foul."`

     * "40"
     * "icon-yellow"
     * "Nicolás De La Cruz (River Plate) is shown the yellow card for a bad foul."
     */
    private fun parseChampionsLeagueEvents(text: String): List<MatchEvent> =
        text.split("\n").mapNotNull { input ->
            runCatching {
                val icon = Regex(ICONS_PATTERN).findAll(input).first().value
                val matchedTime = Regex(TIME_PATTERN).findAll(input).first().value
                val time = matchedTime.replace("'", "")
                val description = input
                    .remove(matchedTime)
                    .remove(icon)
                    .remove("****")
                    .removePrefix("': ")
                    .trim()

                val (isKeyEvent, finalDescription) = parseDescription(description)
                MatchEvent(
                    relativeTime = time,
                    icon = EventIcon.fromString(Regex(INNER_ICON).find(icon)!!.value.removePrefix("#")),
                    description = finalDescription,
                    keyEvent = isKeyEvent
                )
            }.getOrNull()
        }

    private fun parseDescription(description: String): Pair<Boolean, String> {
        val isKeyEvent: Boolean
        val finalDescription = if (description.contains("*")) {
            isKeyEvent = true
            description.replace("\\*".toRegex(), "").trim()
        } else {
            isKeyEvent = false
            description
        }
        return Pair(isKeyEvent, finalDescription)
    }

    private fun String.remove(text: String): String = replace(text, "")

    fun toCommentItemList(
        commentList: List<CommentsEntity>,
        matchStartTime: Long,
    ): Either<Throwable, List<CommentItem>> = catch {
        commentList.map { comment ->
            CommentItem(
                author = comment.author,
                body = comment.body,
                flairUrl = comment.flairUrl,
                flairName = comment.flairText.remove(":"),
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

    fun toCommentSectionListEvents(
        commentList: List<CommentItem>,
        eventList: List<MatchEvent>,
        isRefreshing: Boolean,
    ): Either<Throwable, List<CommentSection>> {

        fun MatchEvent.relativeTime(): Int = runCatching {
            relativeTime.toInt()
        }.getOrNull() ?: run {
            // handles scenarios where event time is like: 45+1, 90+7
            val (fullTime, overtime) = relativeTime.split("+")
            fullTime.toInt() + overtime.toInt()
        }
        return catch {
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

                    while (lastEventTime <= lastCommentTime && commentStack.size > 1) {
                        sectionComments.add(commentStack.last())
                        commentStack.removeLast()
                        lastCommentTime = commentStack.last().relativeTime
                        lastEventTime = eventStack.last().relativeTime()
                    }
                    commentSectionList.add(
                        CommentSection(
                            lastEventTime.toString(),
                            event = eventStack.last(),
                            commentList = if (isRefreshing) {
                                sectionComments
                                    .sortedBy { it.score }
                                    .take(50)
                                    .sortedBy { it.relativeTime }
                            } else {
                                sectionComments
                                    .sortedBy { it.score }
                                    .take(20)
                                    .sortedBy { it.relativeTime }
                            }
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
                Timber.e(it)
            }.getOrNull()!!
        }
    }
}

/**
 * Matches 40' or 45+1
 */
private const val TIME_PATTERN = """((\d)*')(\+((\d)*'))?"""

/**
 * Matches icon pattern: `[](#icon-yellow) or [](#icon-ball)`
 */
private const val ICONS_PATTERN = """\[([^\[\]]*?)]\((\S*?)\)"""

/**
 * Matches icon pattern: `icon-yellow from [](#icon-yellow)'
 */
private const val INNER_ICON = """#(\w)*(-(\w)*)*"""
