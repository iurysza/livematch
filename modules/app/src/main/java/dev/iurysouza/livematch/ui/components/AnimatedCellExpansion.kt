package dev.iurysouza.livematch.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedCellExpansion(
    modifier: Modifier = Modifier,
    showContentIf: () -> Boolean,
    content: @Composable () -> Unit,
    collapsedContent: @Composable () -> Unit = {},
) {
    AnimatedContent(
        targetState = showContentIf(),
        modifier = modifier.background(MaterialTheme.colors.background),
        transitionSpec = {
            if (targetState) {
                fadeIn() with fadeOut() // fadeOut animation if targetState changes to false
            } else {
                fadeIn() with shrinkVertically() // fadeOut animation if targetState changes to false
            }
        }
    ) { show ->
        if (show) {
            content()
        } else {
            collapsedContent()
        }
    }
}
