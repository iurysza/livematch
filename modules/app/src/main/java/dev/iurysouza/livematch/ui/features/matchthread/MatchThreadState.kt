package dev.iurysouza.livematch.ui.features.matchthread

import dev.iurysouza.livematch.ui.features.matchlist.MatchThread

sealed interface PostDetailScreenState {
    data class Success(val post: MatchThread) : PostDetailScreenState
    object Loading : PostDetailScreenState
    data class Error(val msg: String) : PostDetailScreenState
}
