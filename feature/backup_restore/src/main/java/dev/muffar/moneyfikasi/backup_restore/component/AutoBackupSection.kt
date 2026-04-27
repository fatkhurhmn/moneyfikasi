package dev.muffar.moneyfikasi.backup_restore.component

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.domain.model.TimePeriod

@Composable
fun AutoBackupSection(
    isEnabled: Boolean,
    folderUri: String,
    period: TimePeriod,
    onEnabledChange: (Boolean) -> Unit,
    onFolderSelected: (Uri) -> Unit,
    onPeriodSelected: (TimePeriod) -> Unit,
) {
    PrimaryCard {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            AutoBackupSwitch(
                isEnabled = isEnabled,
                folderUri = folderUri,
                onEnabledChange = onEnabledChange,
                onFolderSelected = onFolderSelected
            )
            if (isEnabled) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    AutoBackupFolder(
                        folderUri = folderUri,
                        onFolderSelected = onFolderSelected
                    )
                    AutoBackupPeriod(
                        period = period,
                        onPeriodSelected = onPeriodSelected
                    )
                }
            }
        }
    }
}