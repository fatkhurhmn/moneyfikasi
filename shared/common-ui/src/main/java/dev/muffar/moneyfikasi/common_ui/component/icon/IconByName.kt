package dev.muffar.moneyfikasi.common_ui.component.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.muffar.moneyfikasi.common_ui.utils.IconMapper.toImageVector
import dev.muffar.moneyfikasi.domain.model.AppIcon

@Composable
fun IconByName(
    name: String?,
    modifier: Modifier = Modifier,
    tint: Color,
) {
    val icon = if (!name.isNullOrEmpty()) {
        when (name) {
            Icons.AutoMirrored.Rounded.TrendingDown.name -> Icons.AutoMirrored.Rounded.TrendingDown
            Icons.AutoMirrored.Rounded.TrendingUp.name -> Icons.AutoMirrored.Rounded.TrendingUp
            else -> AppIcon.fromName(name).toImageVector()
        }
    } else {
        Icons.Default.QuestionMark
    }
    Icon(
        imageVector = icon,
        contentDescription = "$name icon",
        modifier = modifier,
        tint = tint
    )
}