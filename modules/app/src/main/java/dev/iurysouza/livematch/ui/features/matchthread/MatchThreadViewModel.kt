package dev.iurysouza.livematch.ui.features.matchthread

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.domain.DomainError
import dev.iurysouza.livematch.domain.matchthread.MatchThreadUseCase
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
    private val matchThreadUseCase: MatchThreadUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<MatchThreadState>(MatchThreadState.Loading)
    val state: StateFlow<MatchThreadState> = _state.asStateFlow()

    private fun mapErrorMsg(error: DomainError?): String = when (error) {
        is UnknownHostException -> resourceProvider.getString(R.string.match_screen_error_no_internet)
        else -> resourceProvider.getString(R.string.match_screen_error_default)
    }

    fun update(match: MatchThread) = viewModelScope.launch {
        _state.value = MatchThreadState.Success(match)
        either {
            matchThreadUseCase.getMatchComments(match.id).bind()
        }.map { comments ->
            comments.map {
                CommentItem(
                    author = it.author,
                    date = it.body,
                    comment = it.body
                )
            }
        }.fold(
            ifLeft = { error ->
                Log.e("LiveMatch", "error: $error")
                _state.emit(MatchThreadState.Error(mapErrorMsg(error)))
            },
            ifRight = { comments ->
                Log.e("LiveMatch", "matches: $comments")
                _state.emit(MatchThreadState.Success(
                    match.copy(comments = comments))
                )
            }
        )
    }

}


