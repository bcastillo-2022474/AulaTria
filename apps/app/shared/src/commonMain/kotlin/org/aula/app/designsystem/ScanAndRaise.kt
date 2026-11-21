package org.aula.app.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Camera viewport for scanning: near-black rounded rectangle, four white corner brackets (44dp, 5dp stroke,
 * inset 18) and a centred [hint]. [preview] is the slot for the live camera, drawn under the brackets.
 */
@Composable
fun ScanFrame(
    hint: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    preview: @Composable () -> Unit = {},
) {
    val shape = RoundedCornerShape(AulaShapes.Camera)
    Box(
        modifier.clip(shape).background(AulaColors.Camera)
            .let { if (onClick != null) it.clickable(role = Role.Button, onClick = onClick) else it }
            .semantics { contentDescription = hint },
        contentAlignment = Alignment.Center,
    ) {
        preview()
        AulaText(hint, AulaTheme.type.cameraHint, Modifier.padding(horizontal = 40.dp), textAlign = TextAlign.Center)
        Canvas(Modifier.fillMaxSize()) {
            val inset = AulaSizes.CornerBracketInset.toPx()
            val len = AulaSizes.CornerBracket.toPx()
            val w = AulaSizes.CornerBracketStroke.toPx()
            val r = AulaShapes.CameraCorner.toPx()
            val half = w / 2
            val c = Offset(size.width / 2, size.height / 2)
            val bracket = Path().apply {
                moveTo(inset + half, inset + len)
                lineTo(inset + half, inset + r)
                arcTo(Rect(Offset(inset + half, inset + half), Offset(inset + 2 * r - half, inset + 2 * r - half)), 180f, 90f, false)
                lineTo(inset + len, inset + half)
            }
            for ((sx, sy) in listOf(1f to 1f, -1f to 1f, 1f to -1f, -1f to -1f)) {
                withTransform({ scale(sx, sy, c) }) { drawPath(bracket, AulaColors.CameraFrame, style = Stroke(w)) }
            }
        }
    }
}

/**
 * The "Levantar la mano" control: 200dp primary disc with 8dp edge, hand icon and two-line label, inside two
 * translucent halos (310dp at 8%, 278dp at 14%).
 */
@Composable
fun RaiseHandButton(onClick: () -> Unit, modifier: Modifier = Modifier, label: String = "Levantar\nla mano", description: String = "Levantar la mano") {
    Box(modifier.size(AulaSizes.RaiseHalo), contentAlignment = Alignment.Center) {
        Box(Modifier.size(AulaSizes.RaiseHalo).background(AulaColors.PulseHaloOuter, CircleShape))
        Box(Modifier.size(AulaSizes.RaiseHalo - AulaSizes.RaiseHaloInset * 2).background(AulaColors.PulseHaloInner, CircleShape))
        PressableFace(
            onClick = onClick,
            fill = AulaColors.Primary,
            edge = AulaColors.PrimaryEdge,
            edgeWidth = AulaSizes.EdgeRaise,
            cornerRadius = AulaSizes.RaiseButton / 2,
            modifier = Modifier.size(AulaSizes.RaiseButton).semantics { contentDescription = description },
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AulaIcon(AulaIcons.Hand, AulaColors.OnFill, size = 44.dp)
                AulaText(label, AulaTheme.type.raiseLabel, color = AulaColors.OnFill, textAlign = TextAlign.Center)
            }
        }
    }
}
