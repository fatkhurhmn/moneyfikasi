package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.muffar.moneyfikasi.common_ui.utils.IconMapper.toImageVector
import dev.muffar.moneyfikasi.domain.model.AppIcon

@Composable
fun IconByName(
    name: String,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    val icon = AppIcon.fromName(name)
    Icon(
        imageVector = icon.toImageVector(),
        contentDescription = "$name icon",
        modifier = modifier,
        tint = tint
    )
}