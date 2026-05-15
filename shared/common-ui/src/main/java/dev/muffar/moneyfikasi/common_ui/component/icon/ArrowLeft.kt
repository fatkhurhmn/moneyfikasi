package dev.muffar.moneyfikasi.common_ui.component.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ArrowLeft() {
    Icon(
        imageVector = Icons.Rounded.ChevronLeft,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}