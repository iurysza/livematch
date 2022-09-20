package dev.iurysouza.livematch.ui.features.matchthread.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.iurysouza.livematch.ui.features.matchthread.MatchEvent


@Composable
fun CircleWith(
    modifier: Modifier = Modifier,
    text: String = "89",
) {
    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .background(Color.Black, shape = CircleShape)
            .layout() { measurable, constraints ->
                // Measure the composable
                val placeable = measurable.measure(constraints)

                //get the current max dimension to assign width=height
                val currentHeight = placeable.height
                var heightCircle = currentHeight
                if (placeable.width > heightCircle)
                    heightCircle = placeable.width

                //assign the dimension and the center position
                layout(heightCircle, heightCircle) {
                    // Where the composable gets placed
                    placeable.placeRelative(0, (heightCircle - currentHeight) / 2)
                }
            }) {

        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = modifier
                .padding(4.dp)
                .defaultMinSize(24.dp) //Use a min size for short text.
        )
    }
}

@Composable
fun SectionHeader(
    sectionName: String,
    event: MatchEvent,
    onClick: (MatchEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(event) }
            .background(Color.White),
        elevation = 4.dp,
    ) {
        Column(
            modifier = modifier
                .padding(vertical = 8.dp, horizontal = 8.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CircleWith(modifier, sectionName)
                Text(
                    event.description,
                    fontWeight = if (event.keyEvent) FontWeight.Bold else FontWeight.Normal
                )
            }
        }

    }
}


@Composable
@Preview
fun CommentHeaderPreview() {
    SectionHeader(
        sectionName = "89",
        event = MatchEvent(
            relativeTime = "89",
            icon = "icon-something",
            description = "Mario Hermoso (Atletico Madrid) is shown the yellow card for a bad foul.",
        ),
        onClick = {},
    )
}
