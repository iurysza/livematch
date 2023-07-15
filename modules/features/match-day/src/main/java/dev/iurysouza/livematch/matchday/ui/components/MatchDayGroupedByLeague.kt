package dev.iurysouza.livematch.matchday.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.iurysouza.livematch.matchday.models.Fakes
import dev.iurysouza.livematch.matchday.models.MatchUiModel
import kotlinx.collections.immutable.ImmutableList

@Preview(
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
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
      .padding(horizontal = 16.dp),
  ) {
    matchList.groupBy { it.competition }.forEach { (competition, matchItemList) ->
      stickyHeader {
        if (shouldUsePlaceHolder) PlaceHolderDivider(competition) else LeagueDivider(competition, Modifier)
      }
      itemsIndexed(matchItemList) { _, matchItem ->
        Column(
          Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onItemTap(matchItem) }
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        ) {
          if (shouldUsePlaceHolder) PlaceHolderItem() else MatchItem(matchItem)
        }
      }
    }
  }
}
