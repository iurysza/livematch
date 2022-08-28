package dev.iurysouza.livematch.ui.features.matchthread

import dev.iurysouza.livematch.ui.features.matchlist.MatchThread

sealed interface PostDetailScreenState {
    data class Success(val author: User, val post: MatchThread) : PostDetailScreenState
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
