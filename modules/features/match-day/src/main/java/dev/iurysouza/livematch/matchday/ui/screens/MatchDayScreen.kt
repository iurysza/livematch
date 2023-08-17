package dev.iurysouza.livematch.matchday.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.iurysouza.livematch.designsystem.components.ErrorScreen
import dev.iurysouza.livematch.designsystem.components.LottieAsset
import dev.iurysouza.livematch.designsystem.components.LottiePullToReveal
import dev.iurysouza.livematch.designsystem.components.gradientBackground
import dev.iurysouza.livematch.matchday.models.MatchDayState
import dev.iurysouza.livematch.matchday.models.MatchDayViewState
import dev.iurysouza.livematch.matchday.models.MatchUiModel
import dev.iurysouza.livematch.matchday.ui.components.MatchDayGroupedByLeague
import dev.iurysouza.livematch.matchday.ui.components.MatchDayTopBar

@Composable
fun MatchDayScreen(
  uiState: MatchDayViewState,
  modifier: Modifier = Modifier,
  onItemTap: (MatchUiModel) -> Unit = {},
  onRefresh: () -> Unit = {},
  onToggleLiveMode: (Boolean) -> Unit = {},
) {
  Column(
    modifier = modifier
      .fillMaxHeight(),
  ) {
    MatchDayTopBar(
      isChecked = uiState.isLiveMode,
      onToggle = onToggleLiveMode,
    )
    LottiePullToReveal(
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
            targetState = uiState.matchDayState,
            label = "MatchDayStateCrossFade",
          ) { state ->
            when (state) {
              is MatchDayState.Error -> ErrorScreen(msg = state.msg)
              is MatchDayState.Empty -> EmptyMatchDay()
              is MatchDayState.Loading -> MatchDayGroupedByLeague(shouldUsePlaceHolder = true)
              is MatchDayState.Success -> MatchDayGroupedByLeague(matchList = state.matches, onItemTap = onItemTap)
            }
          }
        }
      },
    )
  }
}
