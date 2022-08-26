package dev.iurysouza.livematch.data.network

import arrow.core.Either
import arrow.core.Either.Companion.catch
import dev.iurysouza.livematch.BuildConfig
import dev.iurysouza.livematch.domain.DomainError
import dev.iurysouza.livematch.domain.NetworkError
import dev.iurysouza.livematch.domain.adapters.AccessTokenEntity
import dev.iurysouza.livematch.domain.adapters.MatchThreadEntity
import dev.iurysouza.livematch.domain.adapters.NetworkDataSource
import java.util.Base64
import javax.inject.Inject

class RedditNetworkDataSource @Inject constructor(
    private val redditApi: RedditApi,
) : NetworkDataSource {

    override suspend fun getLatestMatchThreadsForToday(): Either<NetworkError, List<MatchThreadEntity>> =
        catch {
            redditApi.getLatestMatchThreadsForToday()
        }.mapLeft { NetworkError(it.message) }
            .map { response ->
                response.data.children.map { child ->
                    MatchThreadEntity(
                        title = child.data.title,
                        url = child.data.url,
                        score = child.data.score,
                        numComments = child.data.numComments,
                        createdAt = child.data.created,
                        content = child.data.selfText ?: "",
                        contentHtml = child.data.selfTextHtml ?: "",
                    )
                }
            }

    override suspend fun getAccessToken(): Either<DomainError, AccessTokenEntity> =
        catch {
            val authorization = getAuthorizationHeader()
            redditApi.getAccessToken(authorization)
        }.mapLeft { NetworkError(it.message) }
            .map {
                AccessTokenEntity(it.accessToken, it.expiresIn, it.deviceId, it.scope, it.tokenType)
            }

    private fun getAuthorizationHeader(): String {
        val username = BuildConfig.CLIENT_ID
        val password = ""
        return "Basic ${Base64.getEncoder().encodeToString("$username:$password".toByteArray())}"
    }

}


