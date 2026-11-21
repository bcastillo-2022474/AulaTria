package org.aula.app.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Row of the speaking queue: position, name, status. [self] highlights the student's own row (tint + 800 weight). */
@Composable
fun QueueRow(position: Int, name: String, status: String, modifier: Modifier = Modifier, self: Boolean = false) {
    Row(
        modifier.fillMaxWidth().background(if (self) AulaColors.PrimaryTint else AulaColors.Surface)
            .heightIn(min = AulaSizes.QueueRowHeight)
            .padding(horizontal = AulaSpacing.RowPadding),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AulaText(position.toString(), AulaTheme.type.queueNumber, Modifier.width(22.dp))
        AulaText(name, if (self) AulaTheme.type.queueNameSelf else AulaTheme.type.queueName, Modifier.weight(1f))
        AulaText(status, AulaTheme.type.small)
    }
}

/** Entry for [QueueList]. */
data class QueueEntry(val name: String, val status: String = "", val self: Boolean = false)

/** The queue card: rows separated by 1dp lines, no outer padding. */
@Composable
fun QueueList(entries: List<QueueEntry>, modifier: Modifier = Modifier) {
    AulaListCard(modifier) {
        entries.forEachIndexed { i, e ->
            if (i > 0) ListDivider()
            QueueRow(i + 1, e.name, e.status, self = e.self)
        }
    }
}
