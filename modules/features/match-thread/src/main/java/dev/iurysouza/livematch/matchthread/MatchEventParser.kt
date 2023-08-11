package dev.iurysouza.livematch.matchthread

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.flatMap
import dev.iurysouza.livematch.matchthread.models.CommentItem
import dev.iurysouza.livematch.matchthread.models.CommentSection
import dev.iurysouza.livematch.matchthread.models.EventIcon
import dev.iurysouza.livematch.matchthread.models.MatchEvent
import dev.iurysouza.livematch.matchthread.models.MatchStatus
import dev.iurysouza.livematch.matchthread.models.Score
import dev.iurysouza.livematch.matchthread.models.ViewError
import dev.iurysouza.livematch.reddit.domain.models.CommentsEntity
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber

open class MatchEventParser {

  fun getMatchEvents(content: String): Pair<List<MatchEvent>, String> {
    val contentList = content.split("\n")
    var headline = contentList.first()
    val scoreValue = headline.substringAfter("[").substringBefore("]")
    runCatching {
      headline = headline.replace(
        Regex(ICONS_PATTERN).findAll(headline).first().value,
        scoreValue,
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
          keyEvent = false,
        ),
      )
      addAll(eventList)
      add(
        MatchEvent(
          relativeTime = "300",
          icon = EventIcon.FinalWhistle,
          description = "Last Comments\n",
          keyEvent = false,
        ),
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
          keyEvent = isKeyEvent,
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

  fun createEventSecionsWithComments(
    commentList: List<CommentsEntity>,
    matchStartTime: Long,
    matchEvents: List<MatchEvent>,
    isRefreshing: Boolean,
  ): Either<Any, List<CommentSection>> = catch {
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
    .mapLeft { ViewError.CommentItemParsingError(it.toString()) }
    .flatMap { commentItemList ->
      toCommentSectionListEvents(
        commentList = commentItemList,
        eventList = matchEvents,
        isRefreshing = isRefreshing,
      )
    }
    .map {
      it.mapIndexed { index, commentSection ->
        if (index == 0) {
          commentSection.copy(event = commentSection.event.copy(relativeTime = ""))
        } else {
          commentSection.copy(
            event = commentSection.event.copy(
              relativeTime = "${commentSection.event.relativeTime}'",
            ),
          )
        }
      }
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

  private fun toCommentSectionListEvents(
    commentList: List<CommentItem>,
    eventList: List<MatchEvent>,
    isRefreshing: Boolean,
    maxNewComments: Int = 200,
    maxComments: Int = 60,
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
                  .take(maxNewComments)
                  .sortedBy { it.relativeTime }
              } else {
                sectionComments
                  .sortedBy { it.score }
                  .take(maxComments)
                  .sortedBy { it.relativeTime }
              }.toImmutableList(),
            ),
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
            add(
              lastSection.copy(
                commentList = (sectionStack.lastOrNull()?.commentList?.reversed() ?: emptyList()).toImmutableList(),
              ),
            )
          }
        }
      }.onFailure {
        Timber.e(it)
      }.getOrNull()!!
    }
  }

  fun parseContent(content: String): MatchStatus {
    val scoreList = runCatching {
      val scores = mutableListOf<List<Score>>()
      val lines = content
        .remove("Pen")
        .trim('\n').split('*').filter { it.isNotBlank() }

      lines.forEach { line ->
        val parts = line.trim().split("scorers:")
        val goalsList = parts[1].remove("(")
          .remove(")")
          .split(',').map { it.trim() }
        val goals = mutableListOf<Score>()

        goalsList.forEach { goalPart ->
          runCatching {
            // Splitting the string based on last occurrence of whitespace
            val lastSpaceIndex = goalPart.lastIndexOf(' ')
            val player = goalPart.substring(0, lastSpaceIndex).trim()
            val timeList = goalPart.substring(lastSpaceIndex).trim()
              .split(',')
              .map { it.remove("'").trim() }

            timeList.forEach { time ->
              goals.add(Score(player = player, minute = time))
            }
          }.onFailure {
            // in case the same player scores multiple goals
            goals.add(
              Score(
                player = goals.last().player,
                minute = goalPart.replace("'", ""),
              ),
            )
          }
        }
        scores.add(goals)
      }
      scores
    }.getOrNull()

    return MatchStatus(
      homeScore = scoreList?.getOrNull(0) ?: emptyList(),
      awayScore = scoreList?.getOrNull(1) ?: emptyList(),
      description = if (scoreList.isNullOrEmpty()) content else "",
    )
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
