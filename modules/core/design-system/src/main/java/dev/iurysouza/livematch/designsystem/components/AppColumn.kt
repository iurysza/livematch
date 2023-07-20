package dev.iurysouza.livematch.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AppColumn(
  modifier: Modifier = Modifier,
  isScrollable: Boolean = true,
  content: @Composable () -> Unit = {},
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .gradientBackground()
      .thenIf(isScrollable) { verticalScroll(rememberScrollState()) },
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    content()
  }
}
