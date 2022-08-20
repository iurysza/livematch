package dev.iurysouza.livematch.data

import dev.iurysouza.livematch.data.models.PostEntity
import dev.iurysouza.livematch.data.models.UserEntity
import retrofit2.http.GET

interface PlaceHolderApi {
    @GET("posts/")
    suspend fun getPosts(): List<PostEntity>

    @GET("users/")
    suspend fun getUsers(): List<UserEntity>
}
