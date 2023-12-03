package dev.iurysouza.livematch.matchthread.ui.components.comments

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.WithStyle
import com.halilibo.richtext.ui.string.RichTextStringStyle
import dev.iurysouza.livematch.designsystem.components.thenIf
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.designsystem.theme.Space.S100
import dev.iurysouza.livematch.designsystem.theme.Space.S150
import dev.iurysouza.livematch.designsystem.theme.Space.S300
import dev.iurysouza.livematch.designsystem.theme.Space.S400
import dev.iurysouza.livematch.designsystem.theme.Space.S50
import dev.iurysouza.livematch.designsystem.theme.Space.S500
import dev.iurysouza.livematch.designsystem.theme.Space.S600
import dev.iurysouza.livematch.matchthread.R
import dev.iurysouza.livematch.matchthread.models.CommentItem


@Composable
fun CommentItemComponent(
  commentItem: CommentItem,
  modifier: Modifier = Modifier,
) {
  val indentationLine = getIndentationLine(
    depth = commentItem.nestedLevel,
    backgroundColor = MaterialTheme.colorScheme.background,
  )
  Row(
    modifier = modifier
      .padding(vertical = S100)
      .thenIf(commentItem.relativeTime == null) {
        padding(horizontal = S300)
      }
      .thenIf(commentItem.relativeTime != null) {
        padding(start = S500 + 4.dp + indentationLine.depth, end = S100)
      }
      .background(indentationLine.color),
  ) {
    Column(
      modifier = Modifier
        .padding(start = S50)
        .background(MaterialTheme.colorScheme.background)
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
    val style = TextStyle(
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.onSurface,
      fontWeight = FontWeight.Normal,
    )
    FlairImage(comment.flairUrl, comment.flairName)
    Text(
      text = comment.author,
      fontWeight = FontWeight.Normal,
      style = style,
    )
    Text(
      text = " • ",
      style = style,
    )
    Text(
      text = comment.relativeTime?.let { stringResource(id = R.string.minutes, comment.relativeTime) } ?: "",
      style = style.copy(fontWeight = FontWeight.Normal),
    )
    Spacer(Modifier.weight(1f))
    Text(
      modifier = Modifier.wrapContentSize(),
      text = comment.score,
      style = style.copy(
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.primary,
      ),
    )
    Text(
      modifier = Modifier.wrapContentSize(),
      text = " pts",
      style = style.copy(fontWeight = FontWeight.Normal),
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
          linkStyle = SpanStyle(color = MaterialTheme.colorScheme.primary),
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
      nestedLevel = 1,
    ),
  )
}

@Composable
private fun getIndentationLine(depth: Int, backgroundColor: Color): CommentIndentationLine = remember(depth) {
  when (depth) {
    0 -> CommentIndentationLine.Depth0(backgroundColor)
    1 -> CommentIndentationLine.Depth1
    2 -> CommentIndentationLine.Depth2
    3 -> CommentIndentationLine.Depth3
    else -> CommentIndentationLine.Depth4
  }
}

private sealed class CommentIndentationLine(
  val color: Color,
  val depth: Dp,
) {
  data class Depth0(private val backgroundColor: Color) : CommentIndentationLine(
    color = backgroundColor,
    depth = 0.dp,
  )

  object Depth1 : CommentIndentationLine(
    color = Color(0xFFFFE100),
    depth = S300,
  )

  object Depth2 : CommentIndentationLine(
    color = Color(0xFF475BC3),
    depth = S400,
  )

  object Depth3 : CommentIndentationLine(
    color = Color(0xFFFF006F),
    depth = S500,
  )

  object Depth4 : CommentIndentationLine(
    color = Color(0xFF14BD89),
    depth = S600,
  )
}
