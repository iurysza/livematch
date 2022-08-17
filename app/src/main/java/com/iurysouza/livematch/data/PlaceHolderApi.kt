package com.iurysouza.livematch.data

import com.iurysouza.livematch.data.models.PostEntity
import com.iurysouza.livematch.data.models.UserEntity
import retrofit2.http.GET

interface PlaceHolderApi {
    @GET("posts/")
    suspend fun getPosts(): List<PostEntity>

    @GET("users/")
    suspend fun getUsers(): List<UserEntity>
}
