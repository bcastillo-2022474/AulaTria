package org.aula.app.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Card that clips rows and dividers (padding 0): the list container of "Mis cursos" and the home actions. */
@Composable
fun AulaListCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) =
    AulaCard(modifier, padding = 0.dp, content = content)

@Composable
fun ListDivider(inset: Dp = 0.dp) {
    Box(Modifier.fillMaxWidth().padding(start = inset).height(1.dp).background(AulaColors.Line))
}

/**
 * Tappable (or static, when [onClick] is null) list row: [leading] chip, title (+ subtitle), [trailing] (chevron by default).
 * Home actions: minHeight 64, [AulaType.rowTitle]. Course list: minHeight 68, [AulaType.rowTitleStrong] + subtitle.
 */
@Composable
fun ListRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = { ChevronEnd() },
    minHeight: Dp = AulaSizes.RowHeightTall,
    titleStyle: TextStyle = AulaTheme.type.rowTitleStrong,
) {
    Row(
        modifier.fillMaxWidth().heightIn(min = minHeight)
            .let { if (onClick != null) it.clickable(role = Role.Button, onClick = onClick) else it }
            .padding(horizontal = AulaSpacing.RowPadding),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leading?.invoke()
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            AulaText(title, titleStyle)
            if (subtitle != null) AulaText(subtitle, AulaTheme.type.caption)
        }
        trailing?.invoke()
    }
}

@Composable
fun ChevronEnd() = AulaIcon(AulaIcons.Chevron, AulaColors.InkSoft, size = 22.dp)

/** Square-ish rounded chip holding an icon or letter. */
@Composable
fun IconChip(size: Dp, radius: Dp, background: Color, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier.size(size).background(background, RoundedCornerShape(radius)), contentAlignment = Alignment.Center) { content() }
}

/** Purple-tint chip with a tinted icon (home actions): 38dp, radius 12. */
@Composable
fun ActionChip(icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) =
    IconChip(38.dp, AulaShapes.ChipSmall, AulaColors.PrimaryTint, modifier) { AulaIcon(icon, AulaColors.Primary, size = 22.dp) }

/** Purple-tint chip with a glyph letter such as "?". */
@Composable
fun ActionChipText(glyph: String, modifier: Modifier = Modifier) =
    IconChip(38.dp, AulaShapes.ChipSmall, AulaColors.PrimaryTint, modifier) { AulaText(glyph, AulaTheme.type.chip20, color = AulaColors.Primary) }

enum class CourseChipSize(val box: Dp, val radius: Dp, val text: (AulaType) -> TextStyle) {
    Small(40.dp, AulaShapes.ChipSmall, { it.chip18 }),
    Medium(44.dp, AulaShapes.ChipMedium, { it.chip20 }),
    Large(52.dp, AulaShapes.ChipLarge, { it.chip24 }),
}

/** Course initial on a colour (Info blue, OkDeep green, ...). */
@Composable
fun CourseChip(letter: String, color: Color, modifier: Modifier = Modifier, size: CourseChipSize = CourseChipSize.Small) =
    IconChip(size.box, size.radius, color, modifier) { AulaText(letter, size.text(AulaTheme.type), color = AulaColors.OnFill) }
