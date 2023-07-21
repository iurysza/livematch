package dev.iurysouza.livematch.matchthread.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.iurysouza.livematch.designsystem.components.roundedClip
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.matchthread.R
import dev.iurysouza.livematch.matchthread.models.FakeFactory
import dev.iurysouza.livematch.matchthread.models.MatchHeader

@Composable
fun CollapsibleToolbar(
  isCollapsed: Boolean,
  matchHeader: MatchHeader,
  navigateUp: () -> Unit = {},
  onTap: () -> Unit = {},
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .height(56.dp)
      .background(MaterialTheme.colors.background),

  ) {
    IconButton(
      onClick = navigateUp,
    ) {
      Icon(
        imageVector = Icons.Filled.ArrowBack,
        tint = MaterialTheme.colors.onPrimary,
        contentDescription = stringResource(R.string.icon_description),
      )
    }
    Score(isCollapsed, onTap, matchHeader)
  }
}

@Composable
private fun Score(
  isCollapsed: Boolean,
  onTap: () -> Unit,
  matchHeader: MatchHeader,
) {
  Row(
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth(),
  ) {
    AnimateIn(visible = isCollapsed) {
      Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onTap() }
          .roundedClip()
          .background(Color.Transparent),
      ) {
        TeamCrest(
          modifier = Modifier.size(24.dp),
          teamCrestUrl = matchHeader.homeTeam.crestUrl,
        )
        Row(
          modifier = Modifier.wrapContentWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          val style = TextStyle(color = MaterialTheme.colors.onPrimary, fontSize = 16.sp)
          Text(text = matchHeader.homeTeam.score, style = style)
          Text(text = "x", style = style)
          Text(text = matchHeader.awayTeam.score, style = style)
        }
        TeamCrest(
          modifier = Modifier.size(24.dp),
          teamCrestUrl = matchHeader.awayTeam.crestUrl,
        )
        Spacer(Modifier.size(1.dp))
      }
    }
  }
}

@Composable
fun AnimateIn(visible: Boolean, content: @Composable () -> Unit) {
  val density = LocalDensity.current
  AnimatedVisibility(
    visible = visible,
    enter = slideInVertically {
      // Slide in from 40 dp from the top.
      with(density) { -40.dp.roundToPx() }
    } + expandVertically(
      // Expand from the top.
      expandFrom = Alignment.Top,
    ) + fadeIn(
      // Fade in with the initial alpha of 0.3f.
      initialAlpha = 0.3f,
    ),
    exit = slideOutVertically() + shrinkVertically() + fadeOut(),
  ) {
    content()
  }
}

@Preview
@Composable
private fun ScreenToolbarCollapsedPreview() = LiveMatchThemePreview {
  CollapsibleToolbar(isCollapsed = true, matchHeader = FakeFactory.generateMatchHeader(), navigateUp = {})
}

@Preview
@Composable
private fun ScreenToolbarPreview() = LiveMatchThemePreview {
  CollapsibleToolbar(isCollapsed = false, matchHeader = FakeFactory.generateMatchHeader(), navigateUp = {})
}
