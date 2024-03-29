package dev.iurysouza.livematch.matchday.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.iurysouza.livematch.designsystem.components.roundedClip
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.designsystem.theme.Space.S100
import dev.iurysouza.livematch.designsystem.theme.Space.S150
import dev.iurysouza.livematch.designsystem.theme.Space.S200
import dev.iurysouza.livematch.matchday.models.Fakes
import dev.iurysouza.livematch.matchday.models.MatchUiModel
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun MatchDayGroupedByLeague(
  modifier: Modifier = Modifier,
  matchList: ImmutableList<MatchUiModel> = Fakes.generateMatchList(6),
  onItemTap: (MatchUiModel) -> Unit = {},
  shouldUsePlaceHolder: Boolean = false,
) {
  LazyColumn(
    modifier = modifier
      .fillMaxHeight(),
  ) {
    matchList.groupBy { it.competition }.forEach { (competition, matchItemList) ->
      item {
        if (shouldUsePlaceHolder) {
          PlaceHolderDivider(Modifier.padding(horizontal = S200))
        } else {
          LeagueDivider(
            competition = competition,
            modifier = Modifier.padding(horizontal = S200),
          )
        }
      }
      itemsIndexed(matchItemList) { _, matchItem ->
        Column(
          Modifier
            .padding(horizontal = S100)
            .fillMaxWidth()
            .roundedClip()
            .clickable { onItemTap(matchItem) }
            .padding(horizontal = S200, vertical = S150),
        ) {
          if (shouldUsePlaceHolder) PlaceHolderItem() else MatchItem(matchItem)
        }
      }
    }
  }
}

@Preview
@Composable
private fun MatchDayGroupedByLeaguePreview() = LiveMatchThemePreview {
  MatchDayGroupedByLeague()
}

@Preview
@Composable
private fun MatchDayGroupedByLeaguePlaceHolderPreview() = LiveMatchThemePreview {
  MatchDayGroupedByLeague(shouldUsePlaceHolder = true)
}
