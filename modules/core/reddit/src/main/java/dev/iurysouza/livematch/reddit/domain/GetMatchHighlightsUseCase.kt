package dev.iurysouza.livematch.reddit.domain

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.reddit.domain.models.MatchHighlightEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMatchHighlightsUseCase @Inject constructor(
    private val networkDataSource: RedditNetworkDataSource,
) {

    suspend operator fun invoke(): Either<DomainError, List<MatchHighlightEntity>> = either {
        networkDataSource.searchFor(
            subreddit = "soccer",
            query = """flair:media OR flair:Mirror""",
            sortBy = "hot",
            timePeriod = "day",
            restrictedToSubreddit = true,
            limit = 100
        ).bind()
    }.map { response -> response.matchHighlightEntities() }

}
