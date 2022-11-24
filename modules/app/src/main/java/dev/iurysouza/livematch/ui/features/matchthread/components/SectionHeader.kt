package dev.iurysouza.livematch.ui.features.matchthread.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.iurysouza.livematch.ui.features.matchthread.EventIcon
import dev.iurysouza.livematch.ui.features.matchthread.MatchEvent
import dev.iurysouza.livematch.ui.theme.AppBackgroundColor
import dev.iurysouza.livematch.ui.theme.TextColor
import dev.iurysouza.livematch.ui.theme.TitleColor

@Composable
fun SectionHeader(
    sectionName: String,
    event: MatchEvent,
    onClick: ((MatchEvent) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    var newModifier = modifier
        .fillMaxWidth()
        .background(AppBackgroundColor)

    if (onClick != null) {
        newModifier = newModifier.clickable { onClick(event) }
    }
    Card(
        modifier = newModifier,
        elevation = 4.dp,
    ) {
        Column(
            modifier = modifier
                .background(AppBackgroundColor)
                .padding(horizontal = 8.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MatchIcon(
                    modifier = modifier,
                    icon = event.icon,
                    time = event.relativeTime,
                )
                Text(
                    event.description,
                    modifier = modifier.align(Alignment.CenterVertically),
                    fontWeight = if (event.keyEvent) FontWeight.Bold else FontWeight.Normal,
                    color = TitleColor
                )
            }
        }

    }
}

@Composable
fun MatchIcon(modifier: Modifier = Modifier, icon: EventIcon, time: String) {
    Column(modifier.background(AppBackgroundColor)) {
        if (time.isNotEmpty()) {
            Box(modifier = modifier
                .align(Alignment.CenterHorizontally)
                .background(color = TextColor)
                .height(16.dp)
                .width(1.dp)
            )
        } else {
            Box(modifier = modifier
                .align(Alignment.CenterHorizontally)
                .height(16.dp)
                .width(1.dp)
            )
        }
        Column(
            modifier.align(Alignment.CenterHorizontally),
        ) {
            Text(
                modifier = modifier.align(Alignment.CenterHorizontally),
                fontSize = 12.sp,
                color = TitleColor,
                text = time,
            )
            MatchEventIcon(
                modifier = modifier.align(Alignment.CenterHorizontally),
                eventIcon = icon,
            )
        }
        Box(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .background(color = TextColor)
                .height(24.dp)
                .width(1.dp)
        )
    }
}

@Composable
private fun MatchEventIcon(modifier: Modifier, eventIcon: EventIcon) {
    Icon(
        tint = TextColor,
        imageVector = eventIcon.toImageVector(),
        contentDescription = "Home",
        modifier = modifier.height(24.dp)
    )
}


@Preview
@Composable
fun MatchIconPreview() {
    MatchIcon(
        modifier = Modifier,
        icon = EventIcon.Goal,
        time = "40"
    )
}


@Composable
@Preview
fun CommentHeaderPreview() {
    SectionHeader(
        sectionName = "89+2",
        event = MatchEvent(
            relativeTime = "89+2",
            icon = EventIcon.FinalWhistle,
            description = "Mario Hermoso (Atletico Madrid) is shown the yellow card for a bad foul.",
        ),
        onClick = {},
    )
}
