package dev.muffar.moneyfikasi.common_ui.component.button.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CommonFilterChip(
    selected: Boolean,
    label: String,
    leadingIcon: @Composable () -> Unit = {},
    onSelect: () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = { onSelect() },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = leadingIcon,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            containerColor = Color.Transparent,
            labelColor = MaterialTheme.colorScheme.onSurface,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        shape = CircleShape,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        )
    )
}