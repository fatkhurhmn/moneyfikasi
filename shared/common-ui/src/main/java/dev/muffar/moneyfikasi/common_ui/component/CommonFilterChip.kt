package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor

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
            selectedContainerColor = MainColor.Blue.extraLight.copy(alpha = 0.5f),
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurface,
            selectedLabelColor = MaterialTheme.colorScheme.primary,
        ),
        shape = CircleShape,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        )
    )
}