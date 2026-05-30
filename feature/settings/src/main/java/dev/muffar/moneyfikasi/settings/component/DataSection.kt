package dev.muffar.moneyfikasi.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material.icons.rounded.History
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingItem
import dev.muffar.moneyfikasi.resource.R

@Composable
fun DataSection(
    onBackupRestoreClick: () -> Unit,
    onExportClick: () -> Unit,
) {
    PrimaryCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            SettingItem(
                title = stringResource(R.string.title_backup_restore),
                subtitle = stringResource(R.string.msg_backup_restore_description),
                icon = Icons.Rounded.History,
                onClick = onBackupRestoreClick
            )
            CommonHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            SettingItem(
                title = stringResource(R.string.title_export_data),
                subtitle = stringResource(R.string.msg_export_data_description),
                icon = Icons.Rounded.FileDownload,
                onClick = onExportClick
            )
        }
    }
}
