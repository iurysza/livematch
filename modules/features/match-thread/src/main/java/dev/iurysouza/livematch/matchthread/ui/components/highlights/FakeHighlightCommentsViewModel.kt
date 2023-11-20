package dev.iurysouza.livematch.matchthread.ui.components.highlights

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.common.MVIViewModel
import dev.iurysouza.livematch.matchthread.models.FakeFactory
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class FakeHighlightCommentsViewModel @Inject constructor() :
  MVIViewModel<HighlightsViewEvent, HighlightsViewState, HighlightsViewEffect>() {
  override fun setInitialState(): HighlightsViewState = HighlightsViewState(HighlightsCommentsViewState.Loading)

  override fun handleEvent(event: HighlightsViewEvent) {
    super.handleEvent(event)
    when (event) {
      is HighlightsViewEvent.GetLatestComments -> getLatestComments()
    }
  }

  private fun getLatestComments() {
    viewModelScope.launch {
      delay(1000)
      setState {
        copy(
          commentSectionState = HighlightsCommentsViewState.Success(
            FakeFactory.commentSection.flatMap { it.commentList }
              .toImmutableList(),
          ),
        )
      }
    }
  }
}
