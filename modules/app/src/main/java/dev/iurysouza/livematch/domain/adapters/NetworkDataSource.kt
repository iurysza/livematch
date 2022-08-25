package dev.iurysouza.livematch.domain.adapters

import arrow.core.Either
import dev.iurysouza.livematch.domain.DomainError
import dev.iurysouza.livematch.domain.auth.AccessTokenEntity
import dev.iurysouza.livematch.domain.matchlist.MatchThreadEntity

interface NetworkDataSource {
    suspend fun getMachThreadList(): Either<DomainError, List<MatchThreadEntity>>
    suspend fun getAccessToken(): Either<DomainError, AccessTokenEntity>
}
