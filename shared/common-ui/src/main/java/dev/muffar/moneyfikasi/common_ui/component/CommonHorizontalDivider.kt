package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun CommonHorizontalDivider(
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = MaterialTheme.colorScheme.outline,
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color
    )
}