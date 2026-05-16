package dev.muffar.moneyfikasi.backup_restore.component

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
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
    PrimaryCard(
        onClick = { onEnabledChange(!isEnabled) }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            AutoBackupSwitch(
                isEnabled = isEnabled,
                folderUri = folderUri,
                onEnabledChange = onEnabledChange,
                onFolderSelected = onFolderSelected
            )
            if (isEnabled) {
                Spacer(modifier = Modifier.height(16.dp))
                CommonHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().padding(start = 64.dp),
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