package dev.iurysouza.livematch.ui.features.matchthread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.domain.adapters.DomainError
import dev.iurysouza.livematch.domain.matchthread.FetchMatchCommentsUseCase
import dev.iurysouza.livematch.util.ResourceProvider
import java.net.UnknownHostException
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MatchThreadViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val fetchMatchComments: FetchMatchCommentsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<MatchThreadState>(MatchThreadState.Loading)
    val state: StateFlow<MatchThreadState> = _state.asStateFlow()

    private fun mapErrorMsg(error: DomainError?): String = when (error) {
        is UnknownHostException -> resourceProvider.getString(R.string.match_screen_error_no_internet)
        else -> resourceProvider.getString(R.string.match_screen_error_default)
    }

    fun update(match: MatchThread) = viewModelScope.launch {
        _state.value = MatchThreadState.Success(match)
        either { fetchMatchComments(match.id).bind() }
            .mapLeft { mapErrorMsg(it) }
            .map { comments ->
                comments.map { CommentItem(it.author, it.body, it.body) }
            }
            .map { match.copy(comments = it) }
            .fold(
                { _state.emit(MatchThreadState.Error(it)) },
                { _state.emit(MatchThreadState.Success(it)) }
            )
    }

}


