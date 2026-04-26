package dev.muffar.moneyfikasi.backup_restore.component

import android.net.Uri
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AutoBackupSection(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    folderUri: String,
    period: TimePeriod,
    onEnabledChange: (Boolean) -> Unit,
    onFolderSelected: (Uri) -> Unit,
    onPeriodSelected: (TimePeriod) -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
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