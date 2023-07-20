package dev.iurysouza.livematch.matchthread

import androidx.compose.foundation.background
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
import dev.iurysouza.livematch.matchthread.components.MatchHeaderNew
import dev.iurysouza.livematch.matchthread.components.ScreenToolbar
import dev.iurysouza.livematch.matchthread.components.isValueTrueForKey
import dev.iurysouza.livematch.matchthread.components.itemCommentList
import dev.iurysouza.livematch.matchthread.components.toggleValue
import dev.iurysouza.livematch.matchthread.models.MatchHeader
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewState

@Composable
fun MatchThreadScreenNew(
  uiState: MatchThreadViewState,
  matchHeader: MatchHeader,
  modifier: Modifier = Modifier,
  onNavigateUp: () -> Unit = {},
  onRefresh: () -> Unit = {},
) {
  var expandedSectionMap by remember { mutableStateOf(mapOf<String, Boolean>()) }
  val state = uiState.descriptionState

  LottiePullToReveal(
    modifier = modifier,
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
          item { MatchHeaderNew(matchHeader) }
          item { MatchDescription(state) }
          itemCommentList(
            state = uiState.commentSectionState,
            expandedSectionMap = expandedSectionMap,
            onSectionTapped = { sectionName ->
              expandedSectionMap = expandedSectionMap.toggleValue(sectionName)
            },
            expandCommentsIf = { sectionName ->
              expandedSectionMap.isValueTrueForKey(sectionName)
            },
            onInit = { expandedSectionMap = it },
          )
        },
      )
    },
  )
  ScreenToolbar(onNavigateUp)
}


