package dev.muffar.moneyfikasi.common_ui.component.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DoubleOutlinedButton(
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
            onClick = onLeftClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        CommonOutlinedButton(
            text = rightText,
            modifier = Modifier.weight(1f),
            onClick = onRightClick
        )
    }
}