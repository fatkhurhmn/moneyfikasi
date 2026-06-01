package dev.muffar.moneyfikasi.common_ui.component.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonButton
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonOutlinedButton

@Composable
fun RowErrorSecondaryButton(
    leftText: String,
    rightText: String,
    modifier: Modifier = Modifier,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit
) {
    Row(modifier) {
        CommonOutlinedButton(
            text = leftText,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.error,
            onClick = onLeftClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        CommonButton(
            text = rightText,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSecondary
            ),
            onClick = onRightClick
        )
    }
}