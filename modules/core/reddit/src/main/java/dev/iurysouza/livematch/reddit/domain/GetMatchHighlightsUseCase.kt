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
    networkDataSource.searchFor(
      subreddit = "soccer",
      query = """flair:media OR flair:Mirror""",
      sortBy = "hot",
      timePeriod = "day",
      restrictedToSubreddit = true,
      limit = 100,
    ).flatMap { response -> response.matchHighlightEntities() }
      .flatMap { matchHighlightParserUseCase.getMatchHighlights(it, matchTitle.value) }
      .bind()
  }
}
