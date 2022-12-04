package dev.iurysouza.livematch.reddit.domain

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.core.DomainError
import dev.iurysouza.livematch.reddit.domain.models.MatchThreadEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchLatestMatchThreadsForTodayUseCase @Inject constructor(
    private val networkDataSource: RedditNetworkDataSource,
) {

    suspend operator fun invoke(): Either<DomainError, List<MatchThreadEntity>> = either {
        networkDataSource.searchFor(
            subreddit = "soccer",
            query = """flair:match+thread AND NOT flair:post AND NOT flair:pre""",
            sortBy = "new",
            timePeriod = "day",
            restrictedToSubreddit = true,
        ).bind()
    }.map { response ->
        response.data.children.map { child ->
            MatchThreadEntity(
                id = child.data.id,
                title = child.data.title,
                url = child.data.url,
                score = child.data.score,
                numComments = child.data.numComments,
                createdAt = child.data.createdUtc,
                content = child.data.selfText ?: "",
                contentHtml = child.data.selfTextHtml ?: "",
            )
        }
    }
}
