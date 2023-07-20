package dev.iurysouza.livematch.matchthread.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

object Fake {
  @Suppress("LongMethod", "MaxLineLength")
  fun generateCommentSection(): ImmutableList<CommentSection> {
    val events = listOf(
      "87, icon-sub, Substitution, Borussia Dortmund. Emre Can replaces Anthony Modeste.",
      "85, icon-sub, Substitution, RB Leipzig. André Silva replaces Timo Werner.",
      "85, icon-sub, Substitution, RB Leipzig. Kevin Kampl replaces Konrad Laimer.",
      "84, icon-ball, Goal! RB Leipzig 3. Borussia Dortmund 0. " +
        "Amadou Haldara (RB Leipzig) right footed shot from the centre of the box to the high centre of the goal. " +
        "Assisted by Timo Werner.",
      "78, icon-sub, Substitution. RB Leipzig. Amadou Haidara replaces Emil Forsberg.",
      "77, icon-sub, Substitution. RB Leipzig. Josko Gvardiol replaces Abdou Diallo.",
      "75, icon-yellow, Konrad Laimer (RB Leipzig) is shown the yellow card.",
    )

    val commentSections = mutableListOf<CommentSection>()

    for ((i, event) in events.withIndex()) {
      val (relativeTime, icon, description) = event.split(",", limit = 3)
      commentSections.add(
        CommentSection(
          event = MatchEvent(
            description = description.trim(),
            icon = EventIcon.fromString(icon.trim()),
            relativeTime = "${relativeTime.trim()}'",
          ),
          name = "Event $i",
          commentList = persistentListOf(
            CommentItem(
              relativeTime = relativeTime.trim().toInt() - i,
              author = "Iury Souza",
              body = "This is a comment $i",
              flairName = "Flamengo",
              flairUrl = "https://a.espncdn.com/combiner/i?img=/i/teamlogos/soccer/500/1039.png",
              score = "$i",
            ),
          ),
        ),
      )
    }

    return commentSections.toImmutableList()
  }
}
