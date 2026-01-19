package dev.muffar.moneyfikasi.common_ui.component.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RowNegativePositiveButton(
    negativeText: String,
    positiveText: String,
    modifier: Modifier = Modifier,
    onNegativeClick: () -> Unit,
    onPositiveClick: () -> Unit
) {
    Row(modifier) {
        CommonOutlinedButton(
            text = negativeText,
            modifier = Modifier.weight(1f),
            onClick = onNegativeClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        CommonButton(
            text = positiveText,
            modifier = Modifier.weight(1f),
            onClick = onPositiveClick
        )
    }
}