package dev.iurysouza.livematch.ui.theme.livematchassets

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.iurysouza.livematch.ui.theme.LiveMatchAssets

public val LiveMatchAssets.Sub: ImageVector
    get() {
        if (_sub != null) {
            return _sub!!
        }
        _sub = Builder(name = "Sub", defaultWidth = 423.755.dp, defaultHeight = 423.755.dp,
                viewportWidth = 423.755f, viewportHeight = 423.755f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(43.84f, 281.457f)
                curveToRelative(-18.585f, -44.869f, -18.586f, -94.29f, 0.0f, -139.159f)
                curveToRelative(10.649f, -25.709f, 26.678f, -48.152f, 46.86f, -66.135f)
                lineToRelative(60.86f, 60.86f)
                verticalLineTo(15.099f)
                horizontalLineTo(29.635f)
                lineToRelative(39.88f, 39.88f)
                curveToRelative(-64.293f, 58.426f, -88.5f, 153.2f, -53.391f, 237.959f)
                curveToRelative(14.167f, 34.202f, 37.07f, 64.159f, 66.234f, 86.634f)
                curveToRelative(28.275f, 21.789f, 61.873f, 36.201f, 97.162f, 41.677f)
                lineToRelative(4.601f, -29.646f)
                curveTo(120.778f, 381.774f, 68.337f, 340.597f, 43.84f, 281.457f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(407.516f, 292.938f)
                curveToRelative(21.652f, -52.272f, 21.652f, -109.848f, 0.0f, -162.12f)
                curveToRelative(-14.167f, -34.202f, -37.071f, -64.159f, -66.234f, -86.633f)
                curveTo(313.007f, 22.395f, 279.409f, 7.983f, 244.12f, 2.507f)
                lineToRelative(-4.601f, 29.646f)
                curveToRelative(63.342f, 9.829f, 115.783f, 51.005f, 140.28f, 110.146f)
                curveToRelative(18.586f, 44.869f, 18.586f, 94.29f, 0.0f, 139.159f)
                curveToRelative(-10.649f, 25.709f, -26.678f, 48.152f, -46.859f, 66.135f)
                lineToRelative(-60.86f, -60.86f)
                verticalLineToRelative(121.924f)
                horizontalLineToRelative(121.924f)
                lineToRelative(-39.801f, -39.801f)
                curveTo(377.118f, 348.099f, 395.334f, 322.348f, 407.516f, 292.938f)
                close()
            }
        }
        .build()
        return _sub!!
    }

private var _sub: ImageVector? = null
