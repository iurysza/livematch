package dev.iurysouza.livematch.ui.features.matchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MatchItem(
    title: String,
    body: String,
    bgColor: Long,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = Modifier
          .height(160.dp)
          .padding(vertical = 4.dp)
          .fillMaxWidth()
          .clickable(onClick = onClick),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .background(color = Color(bgColor)),
        ) {
            TitleText(title)
            LabelText(body)
        }
    }
}

@Composable
private fun LabelText(label: String) {
    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.W400,
                    color = Color(0xC4FFFFFF),
                ),
            ) {
                append(label)
            }
        },
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(start = 8.dp, top = 16.dp),
        fontSize = 16.sp,
        textAlign = TextAlign.Left,
    )
}

@Composable
private fun TitleText(value: String) {
    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.W600,
                    color = Color(0xFFFFFFFF)
                )
            ) {
                append(value)
            }
        },
        modifier = Modifier.padding(horizontal = 8.dp),
        fontSize = 19.sp,
        textAlign = TextAlign.Left,
    )
}

@Preview
@Composable
fun PostItemPreview() {
    MatchItem(
        title = "25.0",
        body = "Label",
        0xFFc92a2a,
    ) { }
}

