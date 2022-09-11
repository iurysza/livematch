package dev.iurysouza.livematch.ui.features.matchthread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.domain.adapters.DomainError
import dev.iurysouza.livematch.domain.adapters.models.CommentsEntity
import dev.iurysouza.livematch.domain.matchthread.FetchMatchCommentsUseCase
import dev.iurysouza.livematch.util.ResourceProvider
import java.net.UnknownHostException
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.floor

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
            .map { it.toCommentItem(match.startTime) }
            .map { it.groupByRelativeTime() }
            .fold(
                { _commentsState.emit(MatchCommentsState.Error(it)) },
                { _commentsState.emit(MatchCommentsState.Success(it)) }
            )
    }

    private fun List<CommentItem>.groupByRelativeTime(): List<Pair<String, List<CommentItem>>> {
        return groupBy { comment ->
            floor((comment.relativeTime.toDouble().div(10.0))).toInt()
        }.toList()
            .sortedBy { (relativeTime, _) -> relativeTime }
            .map { (relativeTime, comments) -> "${(relativeTime + 1) * 10} minutes" to comments }
    }

    private fun List<CommentsEntity>.toCommentItem(matchStartTime: Long): List<CommentItem> {
        return map { comment ->
            CommentItem(
                author = comment.author,
                body = comment.body,
                relativeTime = calculateRelativeTime(comment.created, matchStartTime).toString()
            )
        }.sortedBy { it.relativeTime }
    }


    private fun calculateRelativeTime(
        commentTime: Long,
        matchTime: Long,
    ): Double = Duration.between(
        matchTime.toUTCLocalDateTime(),
        commentTime.toUTCLocalDateTime(),
    ).toMinutes().toDouble()

    private fun Long.toUTCLocalDateTime() =
        LocalDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.systemDefault())


}


