package dev.iurysouza.livematch.matchday.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.iurysouza.livematch.designsystem.components.ErrorScreen
import dev.iurysouza.livematch.designsystem.components.LottieAsset
import dev.iurysouza.livematch.designsystem.components.LottiePullToReveal
import dev.iurysouza.livematch.designsystem.theme.gradientBackground
import dev.iurysouza.livematch.matchday.models.MatchDayViewState
import dev.iurysouza.livematch.matchday.models.MatchListState
import dev.iurysouza.livematch.matchday.models.MatchUiModel
import dev.iurysouza.livematch.matchday.ui.components.MatchDayGroupedByLeague
import dev.iurysouza.livematch.matchday.ui.components.MatchDayTopBar

@Composable
fun MatchDayScreen(
  uiState: MatchDayViewState,
  modifier: Modifier = Modifier,
  onItemTap: (MatchUiModel) -> Unit = {},
  onRefresh: () -> Unit = {},
) {

  Scaffold(
    modifier = modifier
      .fillMaxHeight(),
    topBar = { MatchDayTopBar(uiState.isSyncing) },
  ) { paddingValues ->
    LottiePullToReveal(
      modifier = Modifier.padding(paddingValues),
      isRefreshing = uiState.isRefreshing,
      onRefresh = onRefresh,
      lottieAsset = LottieAsset.FootballFans,
      content = {
        Column(
          Modifier
            .gradientBackground()
            .fillMaxHeight(),
        ) {
          Crossfade(
            targetState = uiState.matchListState,
            label = "MatchListStateCrossFade",
          ) { state ->
            when (state) {
              is MatchListState.Error -> ErrorScreen(msg = state.msg)
              is MatchListState.Empty -> EmptyMatchDay()
              is MatchListState.Loading -> MatchDayGroupedByLeague(shouldUsePlaceHolder = true)
              is MatchListState.Success -> MatchDayGroupedByLeague(
                matchItemList = state.matches, onItemTap = onItemTap,
              )
            }
          }
        }
      },
    )
  }
}




