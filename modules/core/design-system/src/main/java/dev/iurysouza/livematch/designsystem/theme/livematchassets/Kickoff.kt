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

public val LiveMatchAssets.Kickoff: ImageVector
    get() {
        if (_kickoff != null) {
            return _kickoff!!
        }
        _kickoff = Builder(name = "Kickoff", defaultWidth = 64.0.dp, defaultHeight = 64.0.dp,
            viewportWidth = 64.0f, viewportHeight = 64.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero) {
                moveTo(32.0f, 0.0f)
                curveTo(14.355f, 0.0f, 0.0f, 14.355f, 0.0f, 32.0f)
                reflectiveCurveToRelative(14.355f, 32.0f, 32.0f, 32.0f)
                curveToRelative(17.645f, 0.0f, 32.0f, -14.355f, 32.0f, -32.0f)
                reflectiveCurveTo(49.645f, 0.0f, 32.0f, 0.0f)
                close()
                moveTo(61.624f, 36.731f)
                lineToRelative(-3.885f, -6.439f)
                lineToRelative(2.681f, -7.88f)
                curveTo(61.439f, 25.425f, 62.0f, 28.647f, 62.0f, 32.0f)
                curveTo(62.0f, 33.61f, 61.869f, 35.189f, 61.624f, 36.731f)
                close()
                moveTo(54.066f, 52.298f)
                curveToRelative(-0.129f, -0.033f, -0.267f, -0.043f, -0.408f, -0.02f)
                lineTo(43.98f, 53.83f)
                curveToRelative(-0.021f, -0.118f, -0.057f, -0.236f, -0.123f, -0.345f)
                lineToRelative(-5.502f, -9.17f)
                lineToRelative(8.896f, -13.7f)
                horizontalLineToRelative(8.428f)
                curveToRelative(0.023f, 0.108f, 0.047f, 0.215f, 0.105f, 0.312f)
                lineToRelative(5.236f, 8.678f)
                curveTo(59.761f, 44.41f, 57.335f, 48.748f, 54.066f, 52.298f)
                close()
                moveTo(43.981f, 55.855f)
                lineToRelative(7.688f, -1.232f)
                curveToRelative(-3.338f, 2.906f, -7.321f, 5.087f, -11.706f, 6.296f)
                lineTo(43.981f, 55.855f)
                close()
                moveTo(12.65f, 9.1f)
                curveToRelative(5.056f, -4.279f, 11.541f, -6.913f, 18.628f, -7.082f)
                curveToRelative(0.052f, 0.138f, 0.126f, 0.268f, 0.24f, 0.376f)
                lineToRelative(5.525f, 5.214f)
                lineToRelative(-2.185f, 8.156f)
                lineToRelative(-14.237f, 5.465f)
                curveToRelative(-0.052f, -0.042f, -0.093f, -0.094f, -0.154f, -0.126f)
                lineToRelative(-8.87f, -4.701f)
                lineTo(12.65f, 9.1f)
                close()
                moveTo(38.386f, 6.124f)
                lineToRelative(-4.283f, -4.042f)
                curveToRelative(3.916f, 0.273f, 7.628f, 1.293f, 10.989f, 2.931f)
                lineTo(38.386f, 6.124f)
                close()
                moveTo(21.93f, 38.737f)
                lineToRelative(-0.816f, -15.554f)
                lineTo(35.655f, 17.6f)
                lineToRelative(9.803f, 12.106f)
                lineToRelative(-8.483f, 13.063f)
                lineTo(21.93f, 38.737f)
                close()
                moveTo(59.305f, 19.596f)
                curveToRelative(-0.031f, 0.054f, -0.072f, 0.098f, -0.093f, 0.159f)
                lineToRelative(-3.015f, 8.86f)
                horizontalLineToRelative(-9.048f)
                lineTo(36.882f, 15.937f)
                lineToRelative(2.113f, -7.887f)
                lineToRelative(8.27f, -1.371f)
                curveToRelative(0.176f, -0.029f, 0.323f, -0.114f, 0.453f, -0.218f)
                curveTo(52.768f, 9.581f, 56.823f, 14.156f, 59.305f, 19.596f)
                close()
                moveTo(10.311f, 11.307f)
                lineToRelative(-0.802f, 5.561f)
                lineTo(4.16f, 20.843f)
                curveTo(5.595f, 17.274f, 7.696f, 14.045f, 10.311f, 11.307f)
                close()
                moveTo(3.056f, 24.127f)
                curveToRelative(0.044f, -0.023f, 0.09f, -0.037f, 0.131f, -0.068f)
                lineToRelative(7.737f, -5.751f)
                lineToRelative(8.158f, 4.323f)
                lineToRelative(0.888f, 16.936f)
                curveToRelative(0.002f, 0.025f, 0.013f, 0.048f, 0.016f, 0.073f)
                lineToRelative(-7.71f, 7.629f)
                curveToRelative(-0.066f, 0.065f, -0.105f, 0.145f, -0.149f, 0.222f)
                lineTo(4.734f, 44.32f)
                curveToRelative(-0.028f, -0.012f, -0.057f, -0.009f, -0.085f, -0.018f)
                curveTo(2.953f, 40.545f, 2.0f, 36.383f, 2.0f, 32.0f)
                curveTo(2.0f, 29.275f, 2.372f, 26.638f, 3.056f, 24.127f)
                close()
                moveTo(6.078f, 47.072f)
                lineToRelative(5.415f, 2.322f)
                lineToRelative(4.141f, 7.729f)
                curveTo(11.72f, 54.564f, 8.44f, 51.119f, 6.078f, 47.072f)
                close()
                moveTo(18.837f, 58.951f)
                curveToRelative(-0.019f, -0.064f, -0.025f, -0.131f, -0.058f, -0.192f)
                lineToRelative(-5.317f, -9.924f)
                curveToRelative(0.076f, -0.043f, 0.155f, -0.08f, 0.22f, -0.145f)
                lineToRelative(8.027f, -7.942f)
                lineToRelative(14.507f, 3.888f)
                lineToRelative(5.927f, 9.879f)
                curveToRelative(0.05f, 0.083f, 0.11f, 0.154f, 0.178f, 0.217f)
                lineToRelative(-5.449f, 6.867f)
                curveTo(35.285f, 61.859f, 33.659f, 62.0f, 32.0f, 62.0f)
                curveTo(27.28f, 62.0f, 22.814f, 60.901f, 18.837f, 58.951f)
                close()
            }
        }
            .build()
        return _kickoff!!
    }

private var _kickoff: ImageVector? = null
