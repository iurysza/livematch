package dev.iurysouza.livematch.ui.features.matchthread

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.ui.features.matchlist.MatchThread
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
        is UnknownHostException -> resourceProvider.getString(R.string.match_screen_error_no_internet)
        else -> resourceProvider.getString(R.string.match_screen_error_default)
    }

    fun update(match: MatchThread) {
        state.value = PostDetailScreenState.Success(match)
    }

    fun fetchComments() {
        Log.e("LiveMatch", "call fetchComments: ")
    }

}


