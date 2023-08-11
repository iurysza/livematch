package dev.iurysouza.livematch.matchthread.ui.components.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.iurysouza.livematch.designsystem.components.liveMatchPlaceHolder
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.designsystem.theme.Space.S100
import dev.iurysouza.livematch.designsystem.theme.Space.S50
import dev.iurysouza.livematch.designsystem.theme.Space.S800
import dev.iurysouza.livematch.matchthread.models.CommentItem
import dev.iurysouza.livematch.matchthread.models.FakeFactory

@Composable
fun CommentItemComponentPlaceHolder(
  commentItem: CommentItem,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .padding(vertical = S100)
      .padding(start = S800, end = S100)
      .background(MaterialTheme.colorScheme.onSurface),
  ) {
    Column(
      modifier = Modifier
        .padding(start = S50)
        .background(MaterialTheme.colorScheme.background)
        .padding(S50),
    ) {
      Text(
        text = commentItem.author,
        modifier = Modifier
          .padding(bottom = S50)
          .liveMatchPlaceHolder(),
      )
      Text(
        text = commentItem.body.take(80),
        modifier = Modifier.liveMatchPlaceHolder(),
      )
    }
  }
}

@Preview
@Composable
fun CommentItemPlaceHolderPreview() = LiveMatchThemePreview {
  CommentItemComponentPlaceHolder(
    commentItem = FakeFactory.commentSection.first().commentList.first(),
  )
}
