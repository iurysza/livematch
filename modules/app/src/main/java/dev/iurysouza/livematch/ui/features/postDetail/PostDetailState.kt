package dev.iurysouza.livematch.ui.features.postDetail

import dev.iurysouza.livematch.ui.features.posts.Post

sealed interface PostDetailScreenState {
    data class Success(val author: User, val post: Post) : PostDetailScreenState
    object Loading : PostDetailScreenState
    data class Error(val msg: String) : PostDetailScreenState
}

data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val website: String,
)
