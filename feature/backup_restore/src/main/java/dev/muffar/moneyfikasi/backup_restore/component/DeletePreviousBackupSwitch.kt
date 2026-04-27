package dev.muffar.moneyfikasi.backup_restore.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.button.CommonSwitch
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.resource.R

@Composable
fun DeletePreviousBackupSwitch(
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
) {
    PrimaryCard {
        CommonSwitch(
            isEnabled = isEnabled,
            onEnabledChange = onEnabledChange,
            title = stringResource(R.string.delete_previous_backup),
            description = stringResource(R.string.delete_previous_backup_description)
        )
    }
}
