package dev.iurysouza.livematch.features.matchthread.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.iurysouza.livematch.features.matchthread.EventIcon
import dev.iurysouza.livematch.features.matchthread.MatchEvent

@Composable
fun SectionHeader(
    modifier: Modifier = Modifier,
    event: MatchEvent,
    onClick: ((MatchEvent) -> Unit)?,
    isExpanded: Boolean = false,
    nestedCommentCount: Int = 0,
) {
    var newModifier = modifier
        .background(MaterialTheme.colors.background)
    if (onClick != null) {
        newModifier = newModifier.clickable { onClick(event) }
    }
    Row(
        modifier = newModifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(modifier = modifier.weight(.85f)) {
            Timeline(
                modifier = modifier,
                icon = event.icon,
                time = event.relativeTime,
                isKeyEvent = event.keyEvent
            )
            HeaderBody(event, modifier)
        }
        CommentCounterIndicator(isExpanded, nestedCommentCount, modifier
            .padding(start = 4.dp, bottom = 14.dp)
            .width(IntrinsicSize.Min))

    }
}

@Composable
private fun RowScope.HeaderBody(
    event: MatchEvent,
    modifier: Modifier,
) {
    Text(
        text = event.description,
        modifier = modifier
            .weight(.15f)
            .padding(start = 4.dp)
            .padding(bottom = 14.dp)
            .align(Alignment.CenterVertically),
        style = if (event.keyEvent) {
            TextStyle(color = MaterialTheme.colors.onPrimary,
                fontWeight = FontWeight.Bold)
        } else {
            TextStyle(color = MaterialTheme.colors.onBackground)
        },
    )
}

@Composable
private fun CommentCounterIndicator(
    isExpanded: Boolean,
    nestedCommentCount: Int,
    modifier: Modifier,
) {
    if (!isExpanded && nestedCommentCount > 0) {
        Box(
            modifier = modifier
                .width(28.dp)
                .height(24.dp)
                .background(MaterialTheme.colors.primaryVariant, RoundedCornerShape(10.dp))
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "+$nestedCommentCount",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = .7f)
                ),
            )
        }
    }
}

@Composable
fun Timeline(modifier: Modifier = Modifier, icon: EventIcon, time: String, isKeyEvent: Boolean) {
    Column(
        modifier.background(MaterialTheme.colors.background)
    ) {
        Column(
            modifier.align(Alignment.CenterHorizontally),
        ) {
            Text(
                modifier = modifier.align(Alignment.CenterHorizontally),
                fontSize = 12.sp,
                color = MaterialTheme.colors.onPrimary,
                text = time,
            )
            MatchEventIcon(
                modifier = modifier.align(Alignment.CenterHorizontally),
                eventIcon = icon,
                isKeyEvent = isKeyEvent,
            )
        }
        Line(
            modifier = modifier.padding(top = 8.dp),
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
private fun ColumnScope.Line(modifier: Modifier, color: Color) {
    Box(modifier = modifier
        .align(Alignment.CenterHorizontally)
        .background(color = color)
        .height(30.dp)
        .width(3.dp)
    )
}

@Composable
private fun MatchEventIcon(modifier: Modifier, eventIcon: EventIcon, isKeyEvent: Boolean) {
    val tint = if (isKeyEvent) {
        MaterialTheme.colors.primaryVariant
    } else {
        MaterialTheme.colors.onPrimary
    }
    Box(
        modifier = modifier
            .background(tint, CircleShape)
            .padding(2.dp)
            .background(MaterialTheme.colors.background, CircleShape)
            .padding(4.dp)
    ) {
        Icon(
            modifier = modifier
                .height(16.dp)
                .padding(1.dp),
            imageVector = eventIcon.toImageVector(),
            contentDescription = "Home",
            tint = tint,
        )
    }
}


@Preview
@Composable
fun MatchIconPreview() {
    Timeline(
        modifier = Modifier,
        icon = EventIcon.KickOff,
        time = "40",
        isKeyEvent = true
    )
}

@Composable
@Preview
fun CommentHeaderPreview2() {
    SectionHeader(
        nestedCommentCount = 23,
        isExpanded = false,
        event = MatchEvent(
            relativeTime = "89+2'",
            icon = EventIcon.Goal,
            description = "Goal! Japan 0, Costa RIca 1, Keysher Fuller (Costa Rica) left footed shot from the centre of the box to the top left corner. Assisted by Yeltsin Tejeda.",
            keyEvent = true,
        ),
        onClick = {},
    )
}

@Composable
@Preview
fun CommentHeaderPreview() {
    SectionHeader(
        nestedCommentCount = 23,
        isExpanded = true,
        event = MatchEvent(
            relativeTime = "75'",
            icon = EventIcon.YellowCard,
            description = "Mario Hermoso (Atletico Madrid) is shown the yellow card for a bad foul.",
            keyEvent = false
        ),
        onClick = {},
    )
}
