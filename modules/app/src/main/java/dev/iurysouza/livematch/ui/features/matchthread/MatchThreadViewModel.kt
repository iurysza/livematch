package dev.iurysouza.livematch.ui.features.matchthread

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.ui.features.matchlist.Post
import dev.iurysouza.livematch.util.ResourceProvider
import java.net.UnknownHostException
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class MatchThreadViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
) : ViewModel() {

    val state = MutableStateFlow<PostDetailScreenState>(PostDetailScreenState.Loading)

    private fun mapErrorMsg(error: Throwable?): String = when (error) {
        is UnknownHostException -> resourceProvider.getString(R.string.post_screen_error_no_internet)
        else -> resourceProvider.getString(R.string.post_screen_error_default)
    }

    fun fetchCommentsFor(post: Post) {
        state.value = PostDetailScreenState.Success(
            author = User(
                name = "Iury Souza",
                id = 0,
                username = "",
                email = "",
                website = "",
            ),
            post
        )
    }

}


