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
import dev.iurysouza.livematch.designsystem.theme.Space.S100
import dev.iurysouza.livematch.designsystem.theme.Space.S200
import dev.iurysouza.livematch.matchday.models.Fakes
import dev.iurysouza.livematch.matchday.models.MatchUiModel
import kotlinx.collections.immutable.ImmutableList

@Preview
@Composable
internal fun MatchDayGroupedByLeague(
  modifier: Modifier = Modifier,
  matchList: ImmutableList<MatchUiModel> = Fakes.generateMatchList(),
  onItemTap: (MatchUiModel) -> Unit = {},
  shouldUsePlaceHolder: Boolean = false,
) {
  LazyColumn(
    modifier = modifier
      .fillMaxHeight()
      .padding(horizontal = S200),
  ) {
    matchList.groupBy { it.competition }.forEach { (competition, matchItemList) ->
      stickyHeader {
        if (shouldUsePlaceHolder) PlaceHolderDivider(competition) else LeagueDivider(competition, Modifier)
      }
      itemsIndexed(matchItemList) { _, matchItem ->
        Column(
          Modifier
            .roundedClip()
            .clickable { onItemTap(matchItem) }
            .padding(vertical = S100, horizontal = S200)
            .fillMaxWidth(),
        ) {
          if (shouldUsePlaceHolder) PlaceHolderItem() else MatchItem(matchItem)
        }
      }
    }
  }
}
