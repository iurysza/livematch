package dev.iurysouza.livematch.matchthread.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
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
import dev.iurysouza.livematch.matchthread.models.MatchHeader
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewState
import dev.iurysouza.livematch.matchthread.ui.components.CollapsibleToolbar
import dev.iurysouza.livematch.matchthread.ui.components.MatchDescription
import dev.iurysouza.livematch.matchthread.ui.components.MatchHeader
import dev.iurysouza.livematch.matchthread.ui.components.comments.isValueTrueForKey
import dev.iurysouza.livematch.matchthread.ui.components.comments.itemCommentList
import dev.iurysouza.livematch.matchthread.ui.components.comments.toggleValue
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
  val listState = rememberLazyListState()
  val scope = rememberCoroutineScope()
  Column(modifier) {
    val isCollapsed = remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }
    CollapsibleToolbar(
      navigateUp = onNavigateUp,
      isCollapsed = isCollapsed.value,
      matchHeader = matchHeader,
      onTap = { scope.launch { listState.animateScrollToItem(0) } },
    )
    LottiePullToReveal(
      isRefreshing = uiState.isRefreshing,
      onRefresh = onRefresh,
      lottieAsset = LottieAsset.FootballFans,
      content = {
        LazyColumn(
          state = listState,
          modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
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
