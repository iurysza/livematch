package dev.iurysouza.livematch.matchthread.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.iurysouza.livematch.designsystem.components.LottieAsset
import dev.iurysouza.livematch.designsystem.components.LottiePullToReveal
import dev.iurysouza.livematch.matchthread.ui.components.CollapsibleToolbar
import dev.iurysouza.livematch.matchthread.ui.components.MatchDescription
import dev.iurysouza.livematch.matchthread.ui.components.MatchHeader
import dev.iurysouza.livematch.matchthread.ui.components.isValueTrueForKey
import dev.iurysouza.livematch.matchthread.ui.components.itemCommentList
import dev.iurysouza.livematch.matchthread.ui.components.toggleValue
import dev.iurysouza.livematch.matchthread.models.MatchHeader
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewState
import kotlinx.coroutines.launch

@Composable
fun MatchThreadScreen(
  uiState: MatchThreadViewState,
  matchHeader: MatchHeader,
  modifier: Modifier = Modifier,
  onNavigateUp: () -> Unit = {},
  onRefresh: () -> Unit = {},
) {
  var expandedSectionMap by remember { mutableStateOf(mapOf<String, Boolean>()) }
  val state = rememberLazyListState()
  val scope = rememberCoroutineScope()
  Column(modifier) {
    val isCollapsed = remember { derivedStateOf { state.firstVisibleItemIndex > 0 } }
    CollapsibleToolbar(
      navigateUp = onNavigateUp,
      isCollapsed = isCollapsed.value,
      matchHeader = matchHeader,
      onTap = { scope.launch { state.animateScrollToItem(0) } },
    )
    LottiePullToReveal(
      isRefreshing = uiState.isRefreshing,
      onRefresh = onRefresh,
      lottieAsset = LottieAsset.FootballFans,
      content = {
        LazyColumn(
          state = state,
          modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize(),
          content = {
            item { MatchHeader(matchHeader) }
            item { MatchDescription(uiState.descriptionState) }
            itemCommentList(
              state = uiState.commentSectionState,
              onToggleStateInit = { expandedSectionMap = it },
              expandedSectionMap = expandedSectionMap,
              expandCommentsIf = { sectionName -> expandedSectionMap.isValueTrueForKey(sectionName) },
              onSectionTapped = { sectionName ->
                expandedSectionMap = expandedSectionMap.toggleValue(sectionName)
              },
            )
          },
        )
      },
    )
  }
}
