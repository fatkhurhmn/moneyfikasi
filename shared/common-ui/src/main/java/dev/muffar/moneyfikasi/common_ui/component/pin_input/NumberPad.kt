package dev.muffar.moneyfikasi.common_ui.component.pin_input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NumberPad(
    modifier: Modifier = Modifier,
    onDigit: (String) -> Unit,
    onBackspace: () -> Unit,
) {
    val rows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "⌫")
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                row.forEach { label ->
                    when {
                        label.isEmpty() -> Spacer(Modifier.weight(1f))
                        label == "⌫" -> NumpadKey(
                            modifier = Modifier.weight(1f),
                            icon = Icons.AutoMirrored.Rounded.Backspace,
                            onClick = onBackspace
                        )

                        else -> NumpadKey(
                            modifier = Modifier.weight(1f),
                            label = label,
                            onClick = { onDigit(label) }
                        )
                    }
                }
            }
        }
    }
}