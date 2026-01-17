package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor

@Composable
fun CommonHorizontalDivider(
    thickness: Dp = DividerDefaults.Thickness,
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = MainColor.ExtraLightGray
    )
}