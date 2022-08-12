package com.iurysouza.hackernews.data.repo

import com.iurysouza.hackernews.DispatcherProvider
import com.iurysouza.hackernews.data.models.NetworkResponse
import com.iurysouza.hackernews.data.PlaceHolderApi
import com.iurysouza.hackernews.data.models.PostEntity
import com.iurysouza.hackernews.data.models.UserEntity
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
