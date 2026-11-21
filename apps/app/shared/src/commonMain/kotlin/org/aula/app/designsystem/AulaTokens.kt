package org.aula.app.designsystem

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * DESIGN TOKENS: the only file where a raw colour, radius or size may be written. Everything else uses
 * AulaColors / AulaShapes / AulaSpacing / AulaSizes / AulaTheme.type. Values come from the student boards
 * (docs/design/Estudiante-C-*.dc.html, 390x844). Names mirror the web tokens (apps/web/src/design-system/tokens.css).
 * Full table and the inconsistencies resolved: docs/design-system/mobile.md.
 */
object AulaColors {
    val Primary = Color(0xFF5B2EE0)
    val PrimaryEdge = Color(0xFF3F1DB0)
    val PrimaryTint = Color(0xFFECE6FF)
    /** Small caps text on a primary fill (banner kicker). */
    val PrimaryKicker = Color(0xFFEDE7FF)
    val Stage = Color(0xFF3A1B96)
    val StageInk = Color(0xFFD9CEF7)
    val Ink = Color(0xFF1C1C1E)
    /** Ink used on top of the yellow accent. */
    val InkDeep = Color(0xFF2A1260)
    val InkSoft = Color(0xFF6C6C70)
    val Paper = Color(0xFFF2F2F7)
    val Surface = Color(0xFFFFFFFF)
    val Line = Color(0xFFE5E5EA)
    /** Border of resting text fields. */
    val LineStrong = Color(0xFFC7C7CC)
    val Accent = Color(0xFFFFC83D)
    /** Kept for web parity; the student boards use [Edge] over the fill instead. */
    val AccentEdge = Color(0xFFD9A700)
    /** Bright green (teacher dashboards). Student "ok" fills use [OkDeep]. */
    val Ok = Color(0xFF3DDC84)
    val OkDeep = Color(0xFF17753D)
    val Warn = Color(0xFFFF7A59)
    /** Amber of the "falta confirmar ubicación" status disc. */
    val Alert = Color(0xFFFF9F0A)
    val Danger = Color(0xFFD02F38)
    val Info = Color(0xFF1F6FE5)

    /** Content on any saturated fill. */
    val OnFill = Color(0xFFFFFFFF)
    /** Darkening laid over a fill to make its pressable bottom edge (rgba(0,0,0,.22)). */
    val Edge = Color(0x38000000)
    /** Disc behind the glyph of a pulse tile (rgba(255,255,255,.3)). */
    val OnFillDisc = Color(0x4DFFFFFF)
    /** Letter disc of a quiz option (rgba(255,255,255,.28)). */
    val OnFillChip = Color(0x47FFFFFF)
    val PulseHaloOuter = Color(0x145B2EE0)
    val PulseHaloInner = Color(0x245B2EE0)
    val ShadowSoft = Color(0x0F000000)
    val Camera = Color(0xFF0F0F10)
    val CameraInk = Color(0xFFF2F1EC)
    val CameraFrame = Color(0xFFFFFFFF)

    // Quiz option slots (A..D): red, blue, yellow, green, like the board.
    val QuizA = Danger
    val QuizB = Info
    val QuizC = Accent
    val QuizD = OkDeep
}

object AulaShapes {
    val Banner = 26.dp
    val Card = 24.dp
    /** Card of the "¿Es este tu curso?" confirmation (the board uses 26 there). */
    val CardLarge = 26.dp
    val CardCompact = 20.dp
    val Button = 18.dp
    val Option = 16.dp
    val Input = 16.dp
    val CodeInput = 20.dp
    val ChipSmall = 12.dp
    val ChipMedium = 14.dp
    val ChipLarge = 16.dp
    val Camera = 24.dp
    val CameraCorner = 14.dp
    val Bar = 6.dp
    val Pill = 999.dp
}

object AulaSpacing {
    val ScreenTop = 56.dp      // the mock status bar; on a device use the safe-area inset instead
    val ScreenSide = 20.dp
    val ScreenBottom = 28.dp
    val ScreenGap = 22.dp
    val TextInset = 6.dp       // titles and captions sit 6 inside the 20 gutter
    val ButtonStack = 6.dp
    val OptionGap = 10.dp
    val TileGap = 16.dp
    val CardPadding = 24.dp
    val CardPaddingCompact = 20.dp
    val RowPadding = 18.dp
    val DividerInset = 72.dp
}

object AulaSizes {
    val MinTouch = 56.dp
    val ButtonHeight = 60.dp
    val SecondaryHeight = 56.dp
    val LinkHeight = 44.dp
    val EdgeButton = 5.dp
    val EdgeBanner = 6.dp
    val EdgeRaise = 8.dp
    val EdgePressed = 1.dp
    val BannerHeight = 76.dp
    val BannerHeightTall = 88.dp
    val ArrowDisc = 44.dp
    val PulseDisc = 56.dp
    val FieldHeight = 56.dp
    val FieldBorder = 1.5.dp
    val CodeFieldHeight = 76.dp
    val CodeFieldBorder = 2.dp
    val RowHeight = 64.dp
    val RowHeightTall = 68.dp
    val OptionHeight = 76.dp
    val OptionHeightResult = 64.dp
    val OptionEdge = 5.dp
    val OptionLetter = 34.dp
    val QueueRowHeight = 60.dp
    val BarHeight = 12.dp
    val StatusDisc = 96.dp
    val StatusIcon = 48.dp
    val StepDisc = 28.dp
    val RaiseButton = 200.dp
    val RaiseHalo = 310.dp
    val RaiseHaloInset = 34.dp
    val CornerBracket = 44.dp
    val CornerBracketInset = 18.dp
    val CornerBracketStroke = 5.dp
    val FocusRing = 3.dp
    val MaxParagraph = 300.dp
    val PhoneWidth = 390.dp
    val PhoneHeight = 844.dp
}

object AulaAlpha {
    val Disabled = 0.5f
    val Dimmed = 0.5f
    val Pressed = 0.75f
}
