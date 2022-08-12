package com.iurysouza.hackernews.ui.features.posts

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface PostScreenState {
    data class Success(val postList: List<Post>) : PostScreenState
    object Loading : PostScreenState
    data class Error(val msg: String) : PostScreenState
}

@Parcelize
data class Post(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int,
    val bgColor: Long,
) : Parcelable

