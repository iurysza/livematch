package dev.iurysouza.livematch.matchthread.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.WithStyle
import com.halilibo.richtext.ui.string.RichTextStringStyle
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.designsystem.theme.Space.S100
import dev.iurysouza.livematch.designsystem.theme.Space.S150
import dev.iurysouza.livematch.designsystem.theme.Space.S50
import dev.iurysouza.livematch.designsystem.theme.Space.S800
import dev.iurysouza.livematch.matchthread.R
import dev.iurysouza.livematch.matchthread.models.CommentItem

@Composable
fun CommentItemComponent(
  commentItem: CommentItem,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .padding(vertical = S100)
      .padding(start = S800, end = S100)
      .background(MaterialTheme.colors.primary),
  ) {
    Column(
      modifier = Modifier
        .padding(start = S50)
        .background(MaterialTheme.colors.background)
        .padding(S50),
    ) {
      CommentHeader(commentItem)
      CommentBody(commentItem.body)
    }
  }
}

@Composable
private fun CommentHeader(
  comment: CommentItem,
  modifier: Modifier = Modifier,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier.fillMaxWidth(),
  ) {
    val authorStyle = TextStyle(
      fontSize = 12.sp,
      color = MaterialTheme.colors.onSurface,
      fontWeight = FontWeight.Bold,
    )
    FlairImage(comment.flairUrl, comment.flairName)
    Text(
      text = comment.author,
      fontWeight = FontWeight.Bold,
      style = authorStyle,
    )
    Text(
      text = " • ",
      style = authorStyle,
    )
    Text(
      text = stringResource(id = R.string.minutes, comment.relativeTime),
      style = authorStyle.copy(fontWeight = FontWeight.Normal),
    )
    Spacer(Modifier.weight(1f))
    Text(
      modifier = Modifier.wrapContentSize(),
      text = comment.score,
      style = authorStyle.copy(
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colors.primary,
      ),
    )
    Text(
      modifier = Modifier.wrapContentSize(),
      text = " pts",
      style = authorStyle.copy(fontWeight = FontWeight.Normal),
    )
  }
}

@Composable
private fun FlairImage(flair: String?, flairName: String) {
  if (flair != null) {
    Box(
      Modifier.padding(horizontal = 2.dp),
    ) {
      AsyncImage(
        modifier = Modifier
          .size(S150)
          .clip(CircleShape),
        model = flair,
        contentDescription = flairName,
      )
    }
  }
}

@Composable
private fun CommentBody(content: String) {
  RichText(
    modifier = Modifier.padding(bottom = S100),
  ) {
    WithStyle(
      style = RichTextStyle(
        stringStyle = RichTextStringStyle(
          linkStyle = SpanStyle(color = MaterialTheme.colors.primary),
        ),
      ),
    ) {
      Markdown(content)
    }
  }
}

@Preview
@Composable
private fun CommentItemComponentPreview() = LiveMatchThemePreview {
  CommentItemComponent(
    commentItem = CommentItem(
      author = "Chaos_Theory_",
      relativeTime = 62,
      body = "# Fede is just getting better and better. Qatar can't come come soon enough.",
      score = "11",
      flairUrl = "https://styles.redditmedia.com/t5_2qjfi/styles/image_widget_purple2x/1q9q3q9q9qj61/",
      flairName = "Federer",
    ),
  )
}
