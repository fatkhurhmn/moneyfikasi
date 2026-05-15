package dev.muffar.moneyfikasi.common_ui.component.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BoxedIcon(
    icon: String?,
    color: Long?,
    modifier: Modifier = Modifier,
    containerSize: Dp? = null,
    iconSize: Dp = 26.dp,
    onClick: (() -> Unit)? = null
) {
    val containerColor = if (color == 0L || color == null) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        Color(color).copy(alpha = 0.2f)
    }

    val iconColor = if (color == 0L || color == null) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        Color(color)
    }

    Box(
        modifier = modifier
            .then(if (containerSize != null) Modifier.size(containerSize) else Modifier)
            .clip(CardDefaults.shape)
            .background(containerColor)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        IconByName(
            name = icon,
            tint = iconColor,
            modifier = Modifier
                .padding(8.dp)
                .size(iconSize)
        )
    }
}