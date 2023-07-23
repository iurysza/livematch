package dev.iurysouza.livematch.designsystem.theme.livematchassets

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.iurysouza.livematch.designsystem.theme.LiveMatchAssets

public val LiveMatchAssets.Booking: ImageVector
  get() {
    if (_booking != null) {
      return _booking!!
    }
    _booking = Builder(
      name = "Booking",
      defaultWidth = 36.0.dp,
      defaultHeight = 36.0.dp,
      viewportWidth = 36.0f,
      viewportHeight = 36.0f,
    ).apply {
      path(
        fill = SolidColor(Color(0xFFFDCB58)),
        stroke = null,
        strokeLineWidth = 0.0f,
        strokeLineCap = Butt,
        strokeLineJoin = Miter,
        strokeLineMiter = 4.0f,
        pathFillType = NonZero,
      ) {
        moveTo(36.0f, 32.0f)
        arcToRelative(4.0f, 4.0f, 0.0f, false, true, -4.0f, 4.0f)
        horizontalLineTo(4.0f)
        arcToRelative(4.0f, 4.0f, 0.0f, false, true, -4.0f, -4.0f)
        verticalLineTo(4.0f)
        arcToRelative(4.0f, 4.0f, 0.0f, false, true, 4.0f, -4.0f)
        horizontalLineToRelative(28.0f)
        arcToRelative(4.0f, 4.0f, 0.0f, false, true, 4.0f, 4.0f)
        verticalLineToRelative(28.0f)
        close()
      }
    }
      .build()
    return _booking!!
  }

private var _booking: ImageVector? = null
