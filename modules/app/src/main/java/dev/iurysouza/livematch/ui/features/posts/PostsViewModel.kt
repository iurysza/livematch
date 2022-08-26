package dev.iurysouza.livematch.ui.features.posts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.domain.DomainError
import dev.iurysouza.livematch.domain.NetworkError
import dev.iurysouza.livematch.domain.auth.AuthUseCase
import dev.iurysouza.livematch.domain.matchlist.MatchListUseCase
import dev.iurysouza.livematch.util.ResourceProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val matchListUseCase: MatchListUseCase,
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    val state = MutableStateFlow<PostScreenState>(PostScreenState.Loading)

    init {
        getMachList()
    }

    private fun getMachList() = viewModelScope.launch {
        either {
            authUseCase.refreshTokenIfNeeded().bind()
            matchListUseCase.getMatches().bind()

        }.fold({
            Log.e("PostsViewModel", "error: ${it}")
            state.emit(PostScreenState.Error(mapErrorMsg(it)))
        },
            { matchList ->
                Log.e("PostsViewModel", "Error getting match list ${matchList}")
                state.emit(PostScreenState.Success(matchList))
            })
    }

    private fun mapErrorMsg(error: DomainError?): String = when (error) {
        is NetworkError -> resourceProvider.getString(R.string.post_screen_error_no_internet)
        else -> resourceProvider.getString(R.string.post_screen_error_default)
    }
}


