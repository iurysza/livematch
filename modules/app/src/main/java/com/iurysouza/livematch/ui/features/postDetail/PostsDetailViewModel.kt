package com.iurysouza.livematch.ui.features.postDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iurysouza.livematch.R
import com.iurysouza.livematch.data.models.UserEntity
import com.iurysouza.livematch.data.repo.Repository
import com.iurysouza.livematch.ui.features.posts.Post
import com.iurysouza.livematch.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.UnknownHostException
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PostsDetailViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val repository: Repository,
) : ViewModel() {

    val state = MutableStateFlow<PostDetailScreenState>(PostDetailScreenState.Loading)

    private fun mapErrorMsg(error: Throwable?): String = when (error) {
        is UnknownHostException -> resourceProvider.getString(R.string.post_screen_error_no_internet)
        else -> resourceProvider.getString(R.string.post_screen_error_default)
    }

    fun loadPostDetail(post: Post) = viewModelScope.launch {
        state.emit(PostDetailScreenState.Loading)
        repository.getUsers().collect { response ->
            state.emit(
                if (response.isSuccess()) {
                    mapSuccess(post, response.data)
                } else {
                    PostDetailScreenState.Error(mapErrorMsg(response.error))
                }
            )
        }
    }

    private fun mapSuccess(
        post: Post,
        response: List<UserEntity>?,
    ): PostDetailScreenState = runCatching {
        val author = getAuthor(post.userId, response!!)
        PostDetailScreenState.Success(author, post)
    }.getOrElse {
        PostDetailScreenState.Error(resourceProvider.getString(R.string.post_error_author_not_found))
    }

    private fun getAuthor(userId: Int, userList: List<UserEntity>): User {
        val postAuthor = userList.first { it.id == userId }
        return User(
            id = postAuthor.id,
            name = postAuthor.name,
            username = postAuthor.username,
            email = postAuthor.email,
            website = postAuthor.website
        )
    }
}


