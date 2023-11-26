package dev.iurysouza.livematch.reddit.domain

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.flatMap
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.reddit.domain.models.MediaEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMatchHighlightsUseCase @Inject constructor(
  private val networkDataSource: RedditNetworkDataSource,
  private val matchHighlightParserUseCase: MatchHighlightParserUseCase,
) {
  suspend fun execute(matchTitle: MatchTitle): Either<DomainError, List<MediaEntity>> = either {
    var after: String? = null

    fetchData(
      matchTitle = matchTitle,
      onRequestFinished = { after = it },
    ).bind().plus(
      fetchData(matchTitle, after).bind(),
    )
  }

  private suspend fun fetchData(
    matchTitle: MatchTitle,
    after: String? = null,
    onRequestFinished: (String?) -> Unit = {},
  ): Either<DomainError, List<MediaEntity>> = networkDataSource.searchFor(
    subreddit = "soccer",
    query = """flair:media OR flair:Mirror""",
    sortBy = "new",
    timePeriod = "week",
    after = after,
    limit = if (after == null) 100 else null,
    restrictedToSubreddit = true,
  )
    .tap { onRequestFinished(it.data.after) }
    .flatMap { response -> response.matchHighlightEntities() }
    .flatMap { matchHighlightParserUseCase.getMatchHighlights(it, matchTitle) }
}
