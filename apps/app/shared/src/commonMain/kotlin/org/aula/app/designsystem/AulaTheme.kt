package org.aula.app.designsystem

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import org.jetbrains.compose.resources.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.aula.app.resources.Res
import org.aula.app.resources.inter
import org.aula.app.resources.nunito
import org.jetbrains.compose.resources.FontResource

/**
 * Type scale of the student boards. Nunito = display (titles, buttons, big numbers), Inter = body.
 * Names say the role; sizes in the comments are the board's px (1px = 1sp here).
 */
/** CSS line boxes centre the glyphs in the line-height; Compose's default distributes proportionally. */
private val CssLine = LineHeightStyle(LineHeightStyle.Alignment.Center, LineHeightStyle.Trim.None)

class AulaType(private val display: FontFamily, private val body: FontFamily) {
    private fun nunito(weight: FontWeight, size: Int, lineHeight: TextUnit = TextUnit.Unspecified, tracking: TextUnit = TextUnit.Unspecified) =
        TextStyle(fontFamily = display, fontWeight = weight, fontSize = size.sp, lineHeight = lineHeight, letterSpacing = tracking, color = AulaColors.Ink, lineHeightStyle = CssLine)

    private fun inter(weight: FontWeight, size: Int, lineHeight: TextUnit = TextUnit.Unspecified, tracking: TextUnit = TextUnit.Unspecified) =
        TextStyle(fontFamily = body, fontWeight = weight, fontSize = size.sp, lineHeight = lineHeight, letterSpacing = tracking, color = AulaColors.Ink, lineHeightStyle = CssLine)

    private val black = FontWeight.Black
    private val tight = (-0.01).em
    private val tall = 1.1.em

    // Display (Nunito 900, -0.01em, line-height 1.1 unless noted)
    val display110 = nunito(black, 110, 0.95.em, tight) // "0:15" on Tu turno
    val display100 = nunito(black, 100, 0.9.em, tight)  // "2.º" queue position
    val hero48 = nunito(black, 48, tall, tight)         // "Presente"
    val hero44 = nunito(black, 44, tall, tight)         // "Correcto"
    val timer40 = nunito(black, 40, tall, tight)        // quiz timer
    val title36 = nunito(black, 36, tall, tight)        // Únete a tu curso, dudas timer chip
    val title34 = nunito(black, 34, tall, tight)        // default screen title
    val title32 = nunito(black, 32, tall, tight)
    val title30 = nunito(black, 30, tall, tight)        // course name on a card
    val heading26 = nunito(black, 26, tall, tight)      // quiz question
    val heading24 = nunito(black, 24, tall, tight)      // quoted question / grouped doubt

    // Nunito, no tracking
    val bannerTitle = nunito(black, 21, tall)
    val bannerTitleTall = nunito(black, 22, tall)
    val pulseTitle = nunito(black, 30, tall)
    val raiseLabel = nunito(black, 24, tall)
    val code = nunito(black, 34, tracking = 0.12.em)
    val button = nunito(FontWeight.ExtraBold, 17)
    val buttonSmall = nunito(FontWeight.ExtraBold, 16)
    val option = nunito(FontWeight.ExtraBold, 17, 1.2.em)
    val pill = nunito(black, 16)                         // "Entrar"
    val badge = nunito(FontWeight.ExtraBold, 12)
    val badgeLarge = nunito(FontWeight.ExtraBold, 13)    // "Elegido"
    val chip18 = nunito(black, 18)
    val chip20 = nunito(black, 20)
    val chip24 = nunito(black, 24)
    val glyph28 = nunito(black, 28)
    val queueNumber = nunito(black, 18)

    // Inter
    val eyebrow = inter(FontWeight.Normal, 13, tracking = 0.04.em).copy(color = AulaColors.InkSoft)
    val kicker = inter(FontWeight.Bold, 13, tracking = 0.06.em).copy(color = AulaColors.PrimaryKicker)
    val paragraph = inter(FontWeight.Normal, 16, 1.5.em).copy(color = AulaColors.InkSoft)
    val note = inter(FontWeight.Normal, 15, 1.45.em).copy(color = AulaColors.InkSoft)
    val caption = inter(FontWeight.Normal, 14).copy(color = AulaColors.InkSoft)
    val small = inter(FontWeight.Normal, 13).copy(color = AulaColors.InkSoft)
    val rowTitle = inter(FontWeight.SemiBold, 17)
    val rowTitleStrong = inter(FontWeight.Bold, 17)
    val queueName = inter(FontWeight.Medium, 17)
    val queueNameSelf = inter(FontWeight.ExtraBold, 17)
    val bodyCard = inter(FontWeight.Normal, 17, 1.45.em)
    val fieldLabel = inter(FontWeight.Bold, 14).copy(color = AulaColors.InkSoft)
    val fieldText = inter(FontWeight.SemiBold, 17)
    val textarea = inter(FontWeight.Medium, 20, 1.4.em)
    val step = inter(FontWeight.Medium, 17)
    val link = inter(FontWeight.Normal, 15).copy(color = AulaColors.InkSoft)
    val cameraHint = inter(FontWeight.Normal, 16).copy(color = AulaColors.CameraInk)
}

private val LocalAulaType = compositionLocalOf<AulaType> { error("wrap the UI in AulaTheme { }") }

object AulaTheme {
    val type: AulaType
        @Composable @ReadOnlyComposable get() = LocalAulaType.current
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun AulaTheme(content: @Composable () -> Unit) {
    // Variable fonts: one file per family, the weight axis picks the cut.
    @Composable fun variable(res: FontResource, w: Int) = Font(res, FontWeight(w), variationSettings = FontVariation.Settings(FontVariation.weight(w)))
    val display = FontFamily(variable(Res.font.nunito, 700), variable(Res.font.nunito, 800), variable(Res.font.nunito, 900))
    val body = FontFamily(
        variable(Res.font.inter, 400), variable(Res.font.inter, 500), variable(Res.font.inter, 600),
        variable(Res.font.inter, 700), variable(Res.font.inter, 800),
    )
    val type = remember(display, body) { AulaType(display, body) }
    CompositionLocalProvider(LocalAulaType provides type, content = content)
}

/** The one text primitive. [color] overrides the style's colour when set. */
@Composable
fun AulaText(
    text: String,
    style: TextStyle = AulaTheme.type.paragraph,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
) = BasicText(
    text = text,
    modifier = modifier,
    style = style.copy(color = if (color == Color.Unspecified) style.color else color, textAlign = textAlign ?: style.textAlign),
    maxLines = maxLines,
    overflow = TextOverflow.Ellipsis,
)
