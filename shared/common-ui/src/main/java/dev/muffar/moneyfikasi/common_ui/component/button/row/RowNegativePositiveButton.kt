package dev.muffar.moneyfikasi.common_ui.component.button.row

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonButton
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonOutlinedButton

@Composable
fun RowNegativePositiveButton(
    negativeText: String,
    positiveText: String,
    modifier: Modifier = Modifier,
    negativeEnabled: Boolean = true,
    positiveEnabled: Boolean = true,
    onNegativeClick: () -> Unit,
    onPositiveClick: () -> Unit
) {
    Row(modifier) {
        CommonOutlinedButton(
            text = negativeText,
            modifier = Modifier.weight(1f),
            enabled = negativeEnabled,
            color = MaterialTheme.colorScheme.error,
            onClick = onNegativeClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        CommonButton(
            text = positiveText,
            modifier = Modifier.weight(1f),
            enabled = positiveEnabled,
            onClick = onPositiveClick
        )
    }
}