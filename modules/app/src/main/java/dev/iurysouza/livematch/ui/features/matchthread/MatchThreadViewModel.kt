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


    private val _commentsState =
        MutableStateFlow<MatchCommentsState>(MatchCommentsState.Loading)
    val commentsState: StateFlow<MatchCommentsState> = _commentsState.asStateFlow()
    private val _state = MutableStateFlow<MatchDescriptionState>(MatchDescriptionState.Loading)
    val state: StateFlow<MatchDescriptionState> = _state.asStateFlow()

    private fun mapErrorMsg(error: DomainError?): String = when (error) {
        is UnknownHostException -> resourceProvider.getString(R.string.match_screen_error_no_internet)
        else -> resourceProvider.getString(R.string.match_screen_error_default)
    }

    fun update(match: MatchThread) = viewModelScope.launch {
        _state.value = MatchDescriptionState.Success(match)
        _commentsState.value = MatchCommentsState.Loading
        either { fetchMatchComments(match.id).bind() }
            .mapLeft { mapErrorMsg(it) }
            .map { comments ->
                comments
                    .map { CommentItem(it.author, it.body, it.body) }
                    .sortedBy { it.date }
            }
            .fold(
                { _commentsState.emit(MatchCommentsState.Error(it)) },
                { _commentsState.emit(MatchCommentsState.Success(it)) }
            )
    }

}


