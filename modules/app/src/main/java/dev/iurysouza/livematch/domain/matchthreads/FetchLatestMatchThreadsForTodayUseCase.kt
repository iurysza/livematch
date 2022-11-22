package dev.iurysouza.livematch.domain.matchthreads

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.domain.adapters.DomainError
import dev.iurysouza.livematch.domain.adapters.NetworkDataSource
import dev.iurysouza.livematch.domain.adapters.models.MatchThreadEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchLatestMatchThreadsForTodayUseCase @Inject constructor(
    private val networkDataSource: NetworkDataSource,
) {

    suspend operator fun invoke(): Either<DomainError, List<MatchThreadEntity>> = either {
        networkDataSource.searchFor(
            subreddit = "soccer",
            query = """flair:match+thread AND NOT flair:post AND NOT flair:pre""",
            sortBy = "hot",
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