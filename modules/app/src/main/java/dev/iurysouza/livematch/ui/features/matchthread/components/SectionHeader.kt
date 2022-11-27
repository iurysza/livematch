package dev.iurysouza.livematch.ui.features.matchthread.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
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
import dev.iurysouza.livematch.ui.features.matchthread.EventIcon
import dev.iurysouza.livematch.ui.features.matchthread.MatchEvent
import dev.iurysouza.livematch.ui.theme.AppAccentColor
import dev.iurysouza.livematch.ui.theme.AppAccentColorDark
import dev.iurysouza.livematch.ui.theme.AppBackgroundColor
import dev.iurysouza.livematch.ui.theme.AppWhite1
import dev.iurysouza.livematch.ui.theme.AppWhite2

@Composable
fun SectionHeader(
    sectionName: String,
    event: MatchEvent,
    onClick: ((MatchEvent) -> Unit)?,
    isExpanded: Boolean = false,
    nestedCommentCount: Int = 0,
    modifier: Modifier = Modifier,
) {
    var newModifier = modifier
        .background(AppBackgroundColor)
    if (onClick != null) {
        newModifier = newModifier.clickable { onClick(event) }
    }
    Row(
        modifier = newModifier
            .padding(horizontal = 8.dp)
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
            Text(
                event.description,
                modifier = modifier
                    .weight(.15f)
                    .padding(start = 4.dp)
                    .padding(bottom = 14.dp)
                    .align(Alignment.CenterVertically),
                style = if (event.keyEvent) TextStyle(color = AppWhite1,
                    fontWeight = FontWeight.Bold) else TextStyle(color = AppWhite2),
            )
        }
        CommentCounterIndicator(isExpanded, nestedCommentCount, modifier
            .padding(start = 4.dp, bottom = 14.dp)
            .width(IntrinsicSize.Min))

    }
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
                .background(AppAccentColorDark, RoundedCornerShape(10.dp))
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "+$nestedCommentCount",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                    color = Color.White
                ),
            )
        }
    }
}

@Composable
fun Timeline(modifier: Modifier = Modifier, icon: EventIcon, time: String, isKeyEvent: Boolean) {
    Column(
        modifier.background(AppBackgroundColor)
    ) {
        Column(
            modifier.align(Alignment.CenterHorizontally),
        ) {
            Text(
                modifier = modifier.align(Alignment.CenterHorizontally),
                fontSize = 12.sp,
                color = AppWhite1,
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
            color = AppWhite2
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
    val tint = if (isKeyEvent) AppAccentColor else AppWhite1
    Box(
        modifier = modifier
            .background(tint, CircleShape)
            .padding(2.dp)
            .background(AppBackgroundColor, CircleShape)
            .padding(4.dp)
    ) {
        Icon(
            tint = tint,
            imageVector = eventIcon.toImageVector(),
            contentDescription = "Home",
            modifier = modifier
                .height(16.dp)
                .padding(1.dp)
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
        sectionName = "",
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
        sectionName = "",
        event = MatchEvent(
            relativeTime = "75'",
            icon = EventIcon.YellowCard,
            description = "Mario Hermoso (Atletico Madrid) is shown the yellow card for a bad foul.",
            keyEvent = false
        ),
        onClick = {},
    )
}
