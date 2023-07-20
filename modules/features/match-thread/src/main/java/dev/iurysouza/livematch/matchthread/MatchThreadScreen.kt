package dev.iurysouza.livematch.matchthread

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.iurysouza.livematch.designsystem.components.LottieAsset
import dev.iurysouza.livematch.designsystem.components.LottiePullToReveal
import dev.iurysouza.livematch.designsystem.theme.Space.S500
import dev.iurysouza.livematch.matchthread.components.MatchDescription
import dev.iurysouza.livematch.matchthread.components.MatchHeader
import dev.iurysouza.livematch.matchthread.components.ScreenToolbar
import dev.iurysouza.livematch.matchthread.components.isValueTrueForKey
import dev.iurysouza.livematch.matchthread.components.itemCommentList
import dev.iurysouza.livematch.matchthread.components.toggleValue
import dev.iurysouza.livematch.matchthread.models.MatchHeader
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewState

@Composable
fun MatchThreadScreen(
  uiState: MatchThreadViewState,
  matchHeader: MatchHeader,
  modifier: Modifier = Modifier,
  onNavigateUp: () -> Unit = {},
  onRefresh: () -> Unit = {},
) {
  var expandedSectionMap by remember { mutableStateOf(mapOf<String, Boolean>()) }
  Box(modifier) {
    LottiePullToReveal(
      isRefreshing = uiState.isRefreshing,
      onRefresh = onRefresh,
      lottieAsset = LottieAsset.FootballFans,
      content = {
        LazyColumn(
          modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
            .padding(top = S500),
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
    ScreenToolbar(onNavigateUp)
  }
}
