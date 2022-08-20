package com.iurysouza.livematch.ui.features.posts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iurysouza.livematch.R
import com.iurysouza.livematch.data.models.PostEntity
import com.iurysouza.livematch.data.repo.Repository
import com.iurysouza.livematch.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.UnknownHostException
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val repository: Repository,
) : ViewModel() {

    val state = MutableStateFlow<PostScreenState>(PostScreenState.Loading)

    init {
        getPostList()
    }

    private fun getPostList() = viewModelScope.launch {
        state.emit(PostScreenState.Loading)
        repository.getPosts().collect { response ->
            state.emit(
                if (response.isSuccess()) PostScreenState.Success(mapToPost(response.data))
                else PostScreenState.Error(mapErrorMsg(response.error))
            )
        }
    }

    private fun mapErrorMsg(error: Throwable?): String = when (error) {
        is UnknownHostException -> resourceProvider.getString(R.string.post_screen_error_no_internet)
        else -> resourceProvider.getString(R.string.post_screen_error_default)
    }

    private fun mapToPost(data: List<PostEntity>?): List<Post> {
        data ?: return emptyList()
        return data.map {
            Post(
                body = it.body.take(120),
                id = it.id,
                title = it.title,
                userId = it.userId,
                bgColor = getPostColor(it.userId)
            )
        }
    }

    private val postColorList = listOf(
        0xFFc92a2a,
        0xFFa61e4d,
        0xFF862e9c,
        0xFF5f3dc4,
        0xFF364fc7,
        0xFF0b7285,
        0xFF087f5b,
        0xFF2b8a3e,
        0xFF5c940d,
        0xFFe67700,
        0xFFd9480f,
    )

    private fun getPostColor(number: Int): Long = postColorList[postColorList.size % number]
}


