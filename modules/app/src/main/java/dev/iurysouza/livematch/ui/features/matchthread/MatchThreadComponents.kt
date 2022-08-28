package dev.iurysouza.livematch.ui.features.matchthread

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.iurysouza.livematch.ui.features.matchlist.MatchThread
import dev.iurysouza.livematch.ui.features.matchlist.MatchItem

@Composable
fun MatchThreadComponent(
    post: MatchThread,
    user: User,
) {
    val modifier = Modifier
    Column(modifier.height(260.dp)) {
        UserComponent(modifier, post, user)
        MatchItem(
            title = post.title,
            body = post.title,
            bgColor = 0
        )
    }
}

@Composable
private fun UserComponent(
    modifier: Modifier,
    post: MatchThread,
    user: User,
) {
    Row(modifier = modifier
        .padding(top = 16.dp)
        .fillMaxWidth()) {
        Column {
            DetailText(user.username,0 )
            DetailText(user.email, 0)
        }
    }
}

@Composable
private fun DetailText(
    text: String,
    bgColor: Long,
) {
    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.W600,
                    color = Color(bgColor)
                )
            ) {
                append(text)
            }
        },
        modifier = Modifier.padding(horizontal = 8.dp),
        fontSize = 19.sp,
        textAlign = TextAlign.Left,
    )
}

@Composable
fun UserAvatar(
    color: Long,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(Color(color))
            .height(45.dp)
            .aspectRatio(1f)
    )
}
