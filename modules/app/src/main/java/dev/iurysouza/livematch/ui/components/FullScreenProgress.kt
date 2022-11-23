package dev.iurysouza.livematch.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.iurysouza.livematch.ui.theme.LineColor

@Composable
fun FullScreenProgress() {
    Box(
        modifier = Modifier.padding(top = 32.dp).fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = LineColor)
    }
}
