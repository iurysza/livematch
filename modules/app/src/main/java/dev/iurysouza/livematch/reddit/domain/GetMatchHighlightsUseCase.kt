package dev.iurysouza.livematch.reddit.domain

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.core.DomainError
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
    }.map { response ->
        response.data.children.map { child ->
            runCatching {
                val data = child.data
                val media = data.media!!.oEmbed!!
                MatchHighlightEntity(
                    parentId = data.id,
                    title = data.title,
                    type = media.type ?: "video",
                    html = media.html,
                    providerName = media.providerName,
                    providerUrl = media.providerUrl,
                    authorName = data.author,
                    authorUrl = media.authorUrl,
                    thumbnailUrl = media.thumbnailUrl,
                    thumbnailWidth = media.thumbnailWidth,
                    thumbnailHeight = media.thumbnailHeight,
                    width = media.width,
                    height = media.height,
                    createdAt = data.createdUtc,
                )
            }.getOrNull() ?: MatchHighlightEntity(
                parentId = child.data.id,
                title = child.data.title,
                type = "video",
                html = child.data.url,
                providerName = "Reddit",
                providerUrl = "other",
                authorName = child.data.author,
                authorUrl = "https://reddit.com/u/${child.data.author}",
                thumbnailUrl = null,
                thumbnailWidth = null,
                thumbnailHeight = null,
                width = null,
                height = null,
                createdAt = child.data.createdUtc,
            )
        }
    }
}
