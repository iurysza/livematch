package dev.iurysouza.livematch.data.repo

import dev.iurysouza.livematch.DispatcherProvider
import dev.iurysouza.livematch.data.PlaceHolderApi
import dev.iurysouza.livematch.data.models.NetworkResponse
import dev.iurysouza.livematch.data.models.PostEntity
import dev.iurysouza.livematch.data.models.UserEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@Singleton
class Repository @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val api: PlaceHolderApi,
) {
    fun getPosts(): Flow<NetworkResponse<List<PostEntity>>> = flow {
        val posts = api.getPosts()
        emit(NetworkResponse.success(posts))
    }.flowOn(dispatchers.io())
        .catch { emit(NetworkResponse.error(it, null)) }

    fun getUsers(): Flow<NetworkResponse<List<UserEntity>>> = flow {
        val users = api.getUsers()
        emit(NetworkResponse.success(users))
    }.flowOn(dispatchers.io())
        .catch { emit(NetworkResponse.error(it, null)) }
}

sealed interface DomainError

object TokenExpired : DomainError
object NetworkError : DomainError
object KeyNotFound : DomainError
object FailedToSave : DomainError
