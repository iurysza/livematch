package dev.iurysouza.livematch.matchthread.components

import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import dev.iurysouza.livematch.designsystem.components.AnimatedCellExpansion
import kotlin.random.Random

@Preview
@Composable
private fun Screen() {
  var showDevSettings by remember { mutableStateOf(false) }
  Column {
    Column(
      Modifier.weight(1f),
    ) {
      AndroidView(
        modifier = Modifier.padding(all = 8.dp),
        factory = { context ->
          FrameLayout(context).apply {
            setBackgroundColor(Color.Red.toArgb())
            layoutParams = FrameLayout.LayoutParams(
              FrameLayout.LayoutParams.MATCH_PARENT,
              FrameLayout.LayoutParams.MATCH_PARENT,
            )
          }
        },
      )
    }
    Column() {
      Button(
        modifier = Modifier
          .height(40.dp)
          .fillMaxWidth(),
        onClick = { showDevSettings = !showDevSettings },
      ) {
        Text(text = "TOGGLE BOTTOM COMPONENT")
      }
      AnimatedCellExpansion(
        showContentIf = { showDevSettings },
        content = {
          Box(
            Modifier
              .fillMaxWidth()
              .height(Random.nextInt(100, 500).dp)
              .background(Color.Green),
          )
        },
      )
    }
  }
}
