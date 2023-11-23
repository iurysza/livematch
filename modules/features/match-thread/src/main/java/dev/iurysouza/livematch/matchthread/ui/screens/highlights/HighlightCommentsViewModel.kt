package dev.iurysouza.livematch.matchthread.ui.screens.highlights

import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.common.MVIViewModel
import dev.iurysouza.livematch.matchthread.models.CommentItem
import dev.iurysouza.livematch.matchthread.ui.components.highlights.HighlightsCommentsViewState
import dev.iurysouza.livematch.matchthread.ui.components.highlights.HighlightsViewEffect
import dev.iurysouza.livematch.matchthread.ui.components.highlights.HighlightsViewEvent
import dev.iurysouza.livematch.matchthread.ui.components.highlights.HighlightsViewState
import dev.iurysouza.livematch.reddit.domain.FetchMatchCommentsUseCase
import dev.iurysouza.livematch.reddit.domain.MatchId
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@HiltViewModel
class HighlightCommentsViewModel @Inject constructor(
  private val fetchMatchComments: FetchMatchCommentsUseCase,
) : MVIViewModel<HighlightsViewEvent, HighlightsViewState, HighlightsViewEffect>() {
  override fun setInitialState(): HighlightsViewState = HighlightsViewState(HighlightsCommentsViewState.Loading)

  override fun handleEvent(event: HighlightsViewEvent) {
    super.handleEvent(event)
    when (event) {
      is HighlightsViewEvent.GetLatestComments -> getLatestComments(event.id)
    }
  }

  private fun getLatestComments(id: String) = viewModelScope.launch {
    setState { copy(state = HighlightsCommentsViewState.Loading) }
    either { fetchMatchComments.execute(MatchId(id)).bind() }.fold(
      ifLeft = {
        setState { copy(state = HighlightsCommentsViewState.Error(it.throwable?.message ?: "")) }
      },
      ifRight = { commentList ->
        setState {
          copy(
            state = HighlightsCommentsViewState.Success(
              commentList.drop(1).map { comment ->
                CommentItem(
                  author = comment.author,
                  body = comment.body,
                  flairUrl = comment.flairUrl,
                  flairName = comment.flairText.remove(":"),
                  relativeTime = null,
                  score = comment.score.toString(),
                )
              }.toImmutableList(),
            ),
          )
        }
      },
    )
  }
}

private fun String.remove(text: String): String = replace(text, "")
