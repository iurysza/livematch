package dev.iurysouza.livematch.matchthread

import dev.iurysouza.livematch.matchthread.models.CommentItem
import dev.iurysouza.livematch.reddit.domain.models.CommentsEntity
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

internal fun List<CommentsEntity>.toCommentItem(matchStartTime: Long? = null, nestedLevel: Int = 0): List<CommentItem> =
  map { comment ->
    CommentItem(
      author = comment.author,
      body = comment.body,
      flairUrl = comment.flairUrl,
      flairName = comment.flairText.remove(":"),
      relativeTime = matchStartTime?.let {
        comment.created.toUTCLocalDateTime().calculatePlayTime(matchStartTime).toInt()
      },
      score = comment.score.toString(),
      nestedLevel = nestedLevel,
      replies = comment.replies.toCommentItem(matchStartTime, nestedLevel + 1),
    )
  }.sortedBy { it.relativeTime }

internal fun String.remove(text: String): String = replace(text, "")
internal fun Long.toUTCLocalDateTime(): LocalDateTime =
  LocalDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.systemDefault())

private fun LocalDateTime.calculatePlayTime(matchTime: Long): String {
  val duration = Duration.between(matchTime.toUTCLocalDateTime(), this)
  val minutes = duration.toMinutes()
  return "${
    if (minutes > 50) {
      minutes - 25
    } else {
      minutes
    }
  }"
}
