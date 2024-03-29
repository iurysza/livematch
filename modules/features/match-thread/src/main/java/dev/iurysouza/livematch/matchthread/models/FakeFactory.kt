package dev.iurysouza.livematch.matchthread.models

import kotlin.random.Random
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList

@Suppress("MagicNumber")
object FakeFactory {
  @Suppress("LongMethod", "MaxLineLength")
  val commentSection: ImmutableList<CommentSection>
    get() {
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

        val comments = mutableListOf<CommentItem>()
        for (j in 0 until 20) {
          comments.add(
            CommentItem(
              relativeTime = relativeTime.trim().toInt() - i,
              author = "Iury Souza",
              body = generateBody(j).take(Random.nextInt(50, 300)),
              flairName = "Flamengo",
              flairUrl = "https://a.espncdn.com/combiner/i?img=/i/teamlogos/soccer/500/1039.png",
              score = "$j",
            ),
          )
        }

        commentSections.add(
          CommentSection(
            event = MatchEvent(
              description = description.trim(),
              icon = EventIcon.fromString(icon.trim()),
              relativeTime = "${relativeTime.trim()}'",
            ),
            name = "Event $i",
            commentList = comments.toPersistentList(),
          ),
        )
      }

      return commentSections.toImmutableList()
    }

  private fun generateBody(j: Int): String = """PSG would not be better without Mbappe.
      In a high press, high line like most other top teams, Mbappe is absolutely crucial. This is a comment $j"""
    .repeat(Random.nextInt(1, 10))

  fun generateMediaList(): ImmutableList<MediaItem> {
    return (0..10).map {
      MediaItem(
        title = "Media $it",
        url = "https://a.espncdn.com/combiner/i?img=/i/teamlogos/soccer/500/1039.png",
        id = Random.nextInt(1, 99).toString(),
      )
    }.toImmutableList()
  }

  fun generateMatchHeader() = MatchHeader(
    homeTeam = HeaderTeam(
      name = "England",
      crestUrl = "https://crests.football-data.org/770.svg",
      score = "3",
    ),
    awayTeam = HeaderTeam(
      name = "England",
      crestUrl = "https://crests.football-data.org/770.svg",
      score = "3",
    ),
    competition = "Premier League",
    competitionLogo = "https://crests.football-data.org/770.svg",
  )

  val emptyMatchStatus
    get() = MatchStatus(
      homeScore = emptyList(),
      awayScore = emptyList(),
      description = matchDescription,
    )

  val matchStatus
    get() = MatchStatus(
      homeScore = listOf(
        Score(
          player = "Kevin De Bruyne",
          minute = "6",
        ),
        Score(
          player = "Bernardo Silva",
          minute = "56",
        ),
      ),
      awayScore = listOf(
        Score(
          player = "Vini Jr",
          minute = "44",
        ),
        Score(
          player = "Rodrygo",
          minute = "75",
        ),
      ),
    )

  private const val matchDescription = """*RB Leipzig scorers: Willi Orban (6')
    Dominik Szoboszlai (45')
    Amadou Haidara (84')*"""
}
