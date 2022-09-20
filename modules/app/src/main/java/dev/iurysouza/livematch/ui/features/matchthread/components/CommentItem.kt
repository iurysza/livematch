package dev.iurysouza.livematch.ui.features.matchthread.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.ui.features.matchthread.CommentItem


@Composable
fun CommentItemComponent(
    commentItem: CommentItem,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: (CommentItem) -> Unit,
) {
    Column(
        modifier
            .background(Color(0xFFECECEC))
            .padding(start = 32.dp)
            .background(Color.Blue)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = modifier
                .padding(start = 4.dp)
                .background(color)
                .padding(horizontal = 4.dp)
        ) {
            Row(
                modifier.fillMaxWidth()
            ) {
                Text(
                    text = commentItem.author,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.points, commentItem.score),
                    fontSize = 12.sp
                )
            }

            RichText() {
                Markdown(commentItem.body)
            }
            Row(
                modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(id = R.string.minutes, commentItem.relativeTime),
                    fontSize = 12.sp,
                )
            }
        }
        Box(modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0xFFECECEC)))
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
        color = Color.White,
        onClick = {}
    )
}
