package dev.iurysouza.livematch.ui.features.matchthread.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.WithStyle
import com.halilibo.richtext.ui.string.RichTextStringStyle
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.ui.features.matchthread.CommentItem
import dev.iurysouza.livematch.ui.theme.AppBackgroundColor
import dev.iurysouza.livematch.ui.theme.AuthorColor
import dev.iurysouza.livematch.ui.theme.LineColor
import dev.iurysouza.livematch.ui.theme.ScoreColor


@Composable
fun CommentItemComponent(
    commentItem: CommentItem,
    modifier: Modifier = Modifier,
    onClick: (CommentItem) -> Unit,
) {
    Column(
        modifier
            .background(AppBackgroundColor)
            .padding(start = 32.dp)
            .padding(vertical = 4.dp)
            .padding(bottom = 2.dp)
            .background(LineColor)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = modifier
                .padding(start = 4.dp)
                .background(AppBackgroundColor)
                .padding(horizontal = 4.dp)
                .padding(vertical = 2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()
            ) {
                val authorStyle = TextStyle(
                    fontSize = 12.sp,
                    color = AuthorColor,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = commentItem.author,
                    fontWeight = FontWeight.Bold,
                    style = authorStyle
                )
                if (commentItem.flairUrl != null) {
                    Box(
                        Modifier
                            .padding(horizontal = 2.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape),
                            model = commentItem.flairUrl,
                            contentDescription = commentItem.flairName
                        )

                    }
                }
                Text(
                    text = " • ",
                    style = authorStyle
                )
                Text(
                    text = stringResource(id = R.string.minutes, commentItem.relativeTime),
                    style = authorStyle.copy(fontWeight = FontWeight.Normal)
                )
                Spacer(modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(ScoreColor, RoundedCornerShape(10.dp))
                        .padding(4.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxSize(),
                        text = commentItem.score,
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            color = Color.White
                        ),
                    )
                }
            }

            CommentBody(commentItem.body)
        }
    }
}

@Composable
private fun CommentBody(content: String) {
    RichText(
        modifier = Modifier.padding(bottom = 8.dp),
    ) {
        WithStyle(
            style = RichTextStyle(
                stringStyle = RichTextStringStyle(
                    linkStyle = SpanStyle(color = LineColor)
                )
            ),
        ) {
            Markdown(content)
        }
    }
}


@Preview
@Composable
fun CommentItemComponentPreview() {
    CommentItemComponent(
        commentItem = CommentItem(
            author = "elrubiojefe",
            relativeTime = 62,
            body = "Fede is just getting better and better. Qatar can't come come soon enough.",
            score = "11",
            flairUrl = "",
            flairName = ""
        ),
        onClick = {}
    )
}
