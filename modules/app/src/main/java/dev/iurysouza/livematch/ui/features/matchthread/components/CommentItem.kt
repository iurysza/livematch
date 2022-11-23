package dev.iurysouza.livematch.ui.features.matchthread.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.ui.features.matchthread.CommentItem
import dev.iurysouza.livematch.ui.theme.AppBackgroundColor
import dev.iurysouza.livematch.ui.theme.AuthorColor
import dev.iurysouza.livematch.ui.theme.LineColor
import dev.iurysouza.livematch.ui.theme.ScoreColor
import dev.iurysouza.livematch.ui.theme.TitleColor


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
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val authorStyle = TextStyle(color = AuthorColor, fontSize = 12.sp)
                Text(
                    text = commentItem.author,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(color = AuthorColor)
                )
                Text(
                    text = " • ",
                    style = authorStyle
                )
                Text(
                    text = stringResource(id = R.string.minutes, commentItem.relativeTime),
                    style = authorStyle
                )
                Spacer(modifier.weight(1f))
                Box(
                    modifier = Modifier.background(ScoreColor, RectangleShape)
                ) {
                    Text(
                        text = commentItem.score,
                        style = TextStyle(color = TitleColor),
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
        modifier = Modifier.padding(8.dp),
    ) {
        Markdown(content)
    }
}


@Preview
@Composable
fun CommentItemComponentPreview() {
    CommentItemComponent(
        commentItem = CommentItem(
            author = "elrubiojefe",
            body = "Fede is just getting better and better. Qatar can't come come soon enough.",
            score = "11",
            relativeTime = 62
        ),
        onClick = {}
    )
}
