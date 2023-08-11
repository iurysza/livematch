package dev.iurysouza.livematch.matchthread.ui.components.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.iurysouza.livematch.designsystem.components.roundedClip
import dev.iurysouza.livematch.designsystem.components.thenIf
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.designsystem.theme.Space.S100
import dev.iurysouza.livematch.designsystem.theme.Space.S150
import dev.iurysouza.livematch.designsystem.theme.Space.S200
import dev.iurysouza.livematch.designsystem.theme.Space.S300
import dev.iurysouza.livematch.designsystem.theme.Space.S50
import dev.iurysouza.livematch.matchthread.models.EventIcon
import dev.iurysouza.livematch.matchthread.models.MatchEvent

@Composable
fun MatchEventSectionHeader(
  modifier: Modifier = Modifier,
  event: MatchEvent,
  onClick: ((MatchEvent) -> Unit)?,
  isExpanded: Boolean = false,
  nestedCommentCount: Int = 0,
) {
  Row(
    modifier = modifier
      .background(MaterialTheme.colorScheme.background)
      .thenIf(onClick != null) {
        padding(horizontal = S100)
          .roundedClip()
          .clickable { onClick?.let { it(event) } }
      }
      .padding(horizontal = S150)
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Row(modifier = Modifier.weight(.85f)) {
      Timeline(
        icon = event.icon,
        time = event.relativeTime,
        isKeyEvent = event.keyEvent,
      )
      HeaderBody(event)
    }
    CommentCounterIndicator(
      isExpanded,
      nestedCommentCount,
      Modifier
        .padding(start = 4.dp, bottom = 14.dp)
        .width(IntrinsicSize.Min),
    )
  }
}

@Composable
private fun RowScope.HeaderBody(
  event: MatchEvent,
  modifier: Modifier = Modifier,
) {
  Text(
    text = event.description,
    modifier = modifier
      .weight(.15f)
      .padding(start = S50)
      .padding(bottom = S200)
      .align(Alignment.CenterVertically),
    style = if (event.keyEvent) {
      TextStyle(
        color = MaterialTheme.colorScheme.onPrimary,
      )
    } else {
      TextStyle(color = MaterialTheme.colorScheme.onBackground)
    },
  )
}

@Composable
private fun CommentCounterIndicator(
  isExpanded: Boolean,
  nestedCommentCount: Int,
  modifier: Modifier = Modifier,
) {
  if (!isExpanded && nestedCommentCount > 0) {
    Box(
      modifier = modifier
        .size(S300)
        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)),
    ) {
      Text(
        modifier = Modifier.align(Alignment.Center),
        text = "+$nestedCommentCount",
        style = TextStyle(
          textAlign = TextAlign.Center,
          fontSize = 10.sp,
          color = Color.White.copy(alpha = .7f),
        ),
      )
    }
  }
}

@Composable
fun Timeline(modifier: Modifier = Modifier, icon: EventIcon, time: String, isKeyEvent: Boolean) {
  Column(
    modifier.background(MaterialTheme.colorScheme.background),
  ) {
    Column(
      Modifier.align(Alignment.CenterHorizontally),
    ) {
      Text(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onPrimary,
        text = time,
      )
      MatchEventIcon(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        eventIcon = icon,
        isKeyEvent = isKeyEvent,
      )
    }
    Line(
      modifier = Modifier.padding(top = 8.dp),
      color = MaterialTheme.colorScheme.onBackground,
    )
  }
}

@Composable
private fun ColumnScope.Line(
  modifier: Modifier = Modifier,
  color: Color,
) {
  Box(
    modifier = modifier
      .align(Alignment.CenterHorizontally)
      .background(color = color)
      .height(30.dp)
      .width(S50),
  )
}

@Composable
private fun MatchEventIcon(
  modifier: Modifier = Modifier,
  eventIcon: EventIcon,
  isKeyEvent: Boolean,
) {
  val tint = if (isKeyEvent) {
    // get material theme color scheme primary tone
    MaterialTheme.colorScheme.secondary
  } else {
    MaterialTheme.colorScheme.onPrimary
  }
  Box(
    modifier = modifier
      .background(tint, CircleShape)
      .padding(2.dp)
      .background(MaterialTheme.colorScheme.background, CircleShape)
      .padding(S50),
  ) {
    Icon(
      modifier = Modifier
        .height(S200)
        .padding(1.dp),
      imageVector = eventIcon.toImageVector(),
      contentDescription = "Home",
      tint = tint,
    )
  }
}

@Preview
@Composable
private fun MatchIconPreview() = LiveMatchThemePreview {
  Timeline(
    modifier = Modifier,
    icon = EventIcon.KickOff,
    time = "40",
    isKeyEvent = true,
  )
}

@Composable
@Preview
private fun SectionHeaderWithNestedCommentsPreview() = LiveMatchThemePreview {
  MatchEventSectionHeader(
    nestedCommentCount = 23,
    isExpanded = false,
    event = MatchEvent(
      relativeTime = "89+2'",
      icon = EventIcon.Goal,
      description = " Goal! Japan 0, Costa RIca 1, Keysher Fuller (Costa Rica) left footed shot from the centre " +
        "of the box to the top left corner. Assisted by Yeltsin Tejeda.",
      keyEvent = true,
    ),
    onClick = {},
  )
}

@Composable
@Preview
private fun SectionHeaderPreview() = LiveMatchThemePreview {
  MatchEventSectionHeader(
    nestedCommentCount = 23,
    isExpanded = true,
    event = MatchEvent(
      relativeTime = "75'",
      icon = EventIcon.YellowCard,
      description = "Mario Hermoso (Atletico Madrid) is shown the yellow card for a bad foul.",
      keyEvent = false,
    ),
    onClick = {},
  )
}
