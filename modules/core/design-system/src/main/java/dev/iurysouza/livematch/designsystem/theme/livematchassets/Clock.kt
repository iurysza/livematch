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

public val LiveMatchAssets.Clock: ImageVector
  get() {
    if (_clock != null) {
      return _clock!!
    }
    _clock = Builder(
      name = "Clock",
      defaultWidth = 466.008.dp,
      defaultHeight = 466.008.dp,
      viewportWidth = 466.008f,
      viewportHeight = 466.008f,
    ).apply {
      path(
        fill = SolidColor(Color(0xFF000000)),
        stroke = null,
        strokeLineWidth = 0.0f,
        strokeLineCap = Butt,
        strokeLineJoin = Miter,
        strokeLineMiter = 4.0f,
        pathFillType = NonZero,
      ) {
        moveTo(233.004f, 0.0f)
        curveTo(104.224f, 0.0f, 0.0f, 104.212f, 0.0f, 233.004f)
        curveToRelative(0.0f, 128.781f, 104.212f, 233.004f, 233.004f, 233.004f)
        curveToRelative(128.782f, 0.0f, 233.004f, -104.212f, 233.004f, -233.004f)
        curveTo(466.008f, 104.222f, 361.796f, 0.0f, 233.004f, 0.0f)
        close()
        moveTo(244.484f, 242.659f)
        lineToRelative(-63.512f, 75.511f)
        curveToRelative(-5.333f, 6.34f, -14.797f, 7.156f, -21.135f, 1.824f)
        curveToRelative(-6.34f, -5.333f, -7.157f, -14.795f, -1.824f, -21.135f)
        lineToRelative(59.991f, -71.325f)
        verticalLineTo(58.028f)
        curveToRelative(0.0f, -8.284f, 6.716f, -15.0f, 15.0f, -15.0f)
        reflectiveCurveToRelative(15.0f, 6.716f, 15.0f, 15.0f)
        verticalLineToRelative(174.976f)
        horizontalLineToRelative(0.0f)
        curveTo(248.004f, 236.536f, 246.757f, 239.956f, 244.484f, 242.659f)
        close()
      }
    }
      .build()
    return _clock!!
  }

private var _clock: ImageVector? = null
