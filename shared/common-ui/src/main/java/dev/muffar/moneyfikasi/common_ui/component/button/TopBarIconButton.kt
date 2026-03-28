package dev.muffar.moneyfikasi.common_ui.component.button

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun TopBarIconButton(
    painter: Painter,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(40.dp)
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = color
        )
    }
}