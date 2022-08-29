package dev.iurysouza.livematch.ui.features.matchlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MatchList(
    matchItemList: List<MatchItem>,
    onClick: (MatchItem) -> Unit,
) {
    LazyColumn {
        itemsIndexed(matchItemList) { _, matchItem ->
            Column(
                Modifier
                    .clickable { onClick(matchItem) }
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .fillMaxWidth()
            ) {
                MatchText(matchItem.title)
                MatchText(matchItem.competition)
            }
        }
    }
}

@Composable
fun MatchText(text: String) {
    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.W600,
                )
            ) {
                append(text)
            }
        },
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        fontSize = 19.sp,
        textAlign = TextAlign.Left,
    )
}
