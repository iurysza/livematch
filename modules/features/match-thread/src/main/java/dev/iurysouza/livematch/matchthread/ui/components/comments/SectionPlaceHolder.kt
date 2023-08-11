package dev.iurysouza.livematch.matchthread.ui.components.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.iurysouza.livematch.designsystem.components.liveMatchPlaceHolder
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.designsystem.theme.Space.S150
import dev.iurysouza.livematch.designsystem.theme.Space.S200
import dev.iurysouza.livematch.designsystem.theme.Space.S50

@Composable
fun SectionPlaceHolder() {
  Row(
    modifier = Modifier
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = S150)
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Row(modifier = Modifier.weight(.85f)) {
      Timeline()
      HeaderBody()
    }
  }
}

@Preview
@Composable
private fun SectionPlaceHolderPreview() = LiveMatchThemePreview {
  SectionPlaceHolder()
}

@Composable
private fun Timeline() {
  Column(
    Modifier.background(MaterialTheme.colorScheme.background),
  ) {
    Column(
      Modifier.align(Alignment.CenterHorizontally),
    ) {
      Text(
        modifier = Modifier
          .padding(bottom = S50)
          .align(Alignment.CenterHorizontally)
          .liveMatchPlaceHolder(),
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onPrimary,
        text = "00:00",
      )
      MatchEventIcon(
        modifier = Modifier.align(Alignment.CenterHorizontally),
      )
    }
    Line(
      modifier = Modifier.padding(top = 8.dp),
      color = MaterialTheme.colorScheme.onBackground,
    )
  }
}

@Composable
private fun MatchEventIcon(
  modifier: Modifier = Modifier,
) {
  val tint = MaterialTheme.colorScheme.onPrimary
  Box(
    modifier = modifier
      .background(tint, CircleShape)
      .padding(2.dp)
      .background(MaterialTheme.colorScheme.background, CircleShape)
      .padding(S50),
  ) {
    Box(
      modifier = Modifier
        .size(S200)
        .background(MaterialTheme.colorScheme.background)
        .liveMatchPlaceHolder(),
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
      .width(S50)
      .liveMatchPlaceHolder(),
  )
}

@Composable
private fun RowScope.HeaderBody(
  modifier: Modifier = Modifier,
) {
  Column(
    modifier =
    modifier
      .align(Alignment.CenterVertically)
      .weight(.15f)
      .padding(start = S50)
      .padding(bottom = S200),

    ) {
    Text(
      text = "Goal Japan 0, Colombia 1.  ",
      modifier = Modifier
        .padding(bottom = 4.dp)
        .liveMatchPlaceHolder(),
      style = TextStyle(color = MaterialTheme.colorScheme.onBackground),
    )
    Text(
      text = "Goal Japan 0, Colombia 1.  Goal Japan 0, Colombia 1.",
      modifier = Modifier
        .liveMatchPlaceHolder(),
      style = TextStyle(color = MaterialTheme.colorScheme.onBackground),
    )
  }
}
