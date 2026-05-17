package dev.muffar.moneyfikasi.backup_restore.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingSwitchItem
import dev.muffar.moneyfikasi.resource.R

@Composable
fun DeletePreviousBackupSwitch(
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
) {
    PrimaryCard {
        SettingSwitchItem(
            isEnabled = isEnabled,
            onEnabledChange = onEnabledChange,
            title = stringResource(R.string.delete_previous_backup),
            subtitle = stringResource(R.string.delete_previous_backup_description),
            icon = Icons.Rounded.DeleteOutline
        )
    }
}
