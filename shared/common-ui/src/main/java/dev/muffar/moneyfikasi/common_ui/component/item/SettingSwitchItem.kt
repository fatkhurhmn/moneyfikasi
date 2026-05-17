package dev.muffar.moneyfikasi.common_ui.component.item

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun SettingSwitchItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
) {
    SettingItem(
        modifier = modifier,
        title = title,
        subtitle = subtitle,
        icon = icon,
        onClick = { onEnabledChange(!isEnabled) },
        trailing = {
            Switch(
                checked = isEnabled,
                onCheckedChange = onEnabledChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                    uncheckedTrackColor = Color.Transparent
                )
            )
        }
    )
}
