package com.iurysouza.hackernews.data

import com.iurysouza.hackernews.data.models.PostEntity
import com.iurysouza.hackernews.data.models.UserEntity
import retrofit2.http.GET

interface PlaceHolderApi {
    @GET("posts/")
    suspend fun getPosts(): List<PostEntity>

    @GET("users/")
    suspend fun getUsers(): List<UserEntity>
}
