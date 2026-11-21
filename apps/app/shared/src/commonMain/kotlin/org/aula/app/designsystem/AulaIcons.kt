package org.aula.app.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Stroke icons on a 24x24 grid, same paths as the inline SVGs of the boards (check, QR, raised hand, alert mark).
 * Arrow and chevron replace the text glyphs "→" and "›" of the boards (font fallback differs per platform).
 * Icons are drawn black and tinted at the use site by [AulaIcon].
 */
object AulaIcons {
    private fun stroke(name: String, width: Float, vararg paths: String): ImageVector =
        ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f).apply {
            for (p in paths) addPath(
                pathData = PathParser().parsePathString(p).toNodes(),
                fill = null,
                stroke = SolidColor(AulaColors.Ink),
                strokeLineWidth = width,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            )
        }.build()

    private fun box(x: Float, y: Float, s: Float, r: Float): String =
        "M${x + r} ${y}h${s - 2 * r}a$r $r 0 0 1 $r ${r}v${s - 2 * r}a$r $r 0 0 1 -$r ${r}h-${s - 2 * r}a$r $r 0 0 1 -$r -${r}v-${s - 2 * r}a$r $r 0 0 1 $r -${r}z"

    /** Status disc and pulse tile check: `M6 12.5l4 4 8-9`. */
    val Check: ImageVector by lazy { stroke("Check", 2.6f, "M6 12.5l4 4 8-9") }
    /** Small check inside the 28dp step disc (stroke 3 on the board). */
    val CheckBold: ImageVector by lazy { stroke("CheckBold", 3f, "M6 12.5l4 4 8-9") }
    val Cross: ImageVector by lazy { stroke("Cross", 2.6f, "M7 7l10 10M17 7L7 17") }
    /** Exclamation of the "falta confirmar" disc. */
    val Alert: ImageVector by lazy { stroke("Alert", 2.6f, "M12 6v7M12 17.5v.5") }
    val Qr: ImageVector by lazy {
        stroke("Qr", 2.2f, box(3f, 3f, 7f, 1.5f), box(14f, 3f, 7f, 1.5f), box(3f, 14f, 7f, 1.5f), "M14 14h3v3M21 14v7h-4")
    }
    val Hand: ImageVector by lazy {
        stroke(
            "Hand", 1.8f,
            "M8 13V5.5a1.5 1.5 0 0 1 3 0V11M11 10V4a1.5 1.5 0 0 1 3 0v6M14 10V5.5a1.5 1.5 0 0 1 3 0V13" +
                "M17 9.5a1.5 1.5 0 0 1 3 0V15a6 6 0 0 1 -6 6h-1.5a6 6 0 0 1 -4.7 -2.3L4.6 14.7a1.5 1.5 0 0 1 2.3 -1.9L8 14",
        )
    }
    val Arrow: ImageVector by lazy { stroke("Arrow", 2.8f, "M5 12h14M13 6l6 6-6 6") }
    val Chevron: ImageVector by lazy { stroke("Chevron", 2.4f, "M9 5l7 7-7 7") }
}

/** Tinted icon. Pass [contentDescription] only when the icon carries meaning alone; decorative icons stay silent. */
@Composable
fun AulaIcon(
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    contentDescription: String? = null,
) {
    Image(
        imageVector = icon,
        contentDescription = null,
        modifier = modifier.size(size).let { m -> if (contentDescription != null) m.semantics { this.contentDescription = contentDescription } else m },
        colorFilter = ColorFilter.tint(tint),
    )
}
