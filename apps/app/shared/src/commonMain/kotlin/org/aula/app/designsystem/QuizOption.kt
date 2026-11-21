package org.aula.app.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/** Fixed colour per answer letter, as on the board: A red, B blue, C yellow, D green. */
enum class QuizSlot(val letter: String, val fill: Color, val ink: Color) {
    A("A", AulaColors.QuizA, AulaColors.OnFill),
    B("B", AulaColors.QuizB, AulaColors.OnFill),
    C("C", AulaColors.QuizC, AulaColors.InkDeep),
    D("D", AulaColors.QuizD, AulaColors.OnFill),
}

/**
 * Idle: answerable. Selected: ring (assumption, not on a board). Correct: full opacity + check.
 * Wrong: 50% + cross (assumption). Dimmed: 50% (the board's non-chosen/non-correct options in the result).
 * Disabled: not tappable (e.g. already answered), 50%.
 */
enum class QuizOptionState { Idle, Selected, Correct, Wrong, Dimmed, Disabled }

/** Quiz answer row: 76dp (64dp in the result), radius 16, 5dp edge, letter disc 34. */
@Composable
fun QuizOptionRow(
    slot: QuizSlot,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: QuizOptionState = QuizOptionState.Idle,
    compact: Boolean = false,
) {
    val dim = state == QuizOptionState.Dimmed || state == QuizOptionState.Wrong || state == QuizOptionState.Disabled
    PressableFace(
        onClick = onClick,
        fill = slot.fill,
        edge = AulaColors.Edge,
        edgeWidth = AulaSizes.OptionEdge,
        cornerRadius = AulaShapes.Option,
        modifier = modifier.fillMaxWidth().alpha(if (dim) AulaAlpha.Dimmed else 1f)
            .semantics(mergeDescendants = true) { role = Role.RadioButton; selected = state == QuizOptionState.Selected },
        minHeight = if (compact) AulaSizes.OptionHeightResult else AulaSizes.OptionHeight,
        enabled = state == QuizOptionState.Idle || state == QuizOptionState.Selected,
        role = Role.RadioButton,
        fadeWhenDisabled = false,
        ringColor = if (state == QuizOptionState.Selected) slot.fill else Color.Unspecified,
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = AulaSpacing.RowPadding),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(Modifier.size(AulaSizes.OptionLetter).background(AulaColors.OnFillChip, CircleShape), contentAlignment = Alignment.Center) {
                AulaText(slot.letter, AulaTheme.type.option, color = slot.ink)
            }
            AulaText(text, AulaTheme.type.option, Modifier.weight(1f), color = slot.ink)
            when (state) {
                QuizOptionState.Correct -> AulaIcon(AulaIcons.Check, slot.ink, size = 24.dp, contentDescription = "Correcta")
                QuizOptionState.Wrong -> AulaIcon(AulaIcons.Cross, slot.ink, size = 24.dp, contentDescription = "Incorrecta")
                else -> {}
            }
        }
    }
}
