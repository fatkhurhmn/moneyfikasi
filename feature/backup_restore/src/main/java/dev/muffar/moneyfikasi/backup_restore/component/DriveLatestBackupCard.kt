package dev.muffar.moneyfikasi.backup_restore.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingItem
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LongExt.formattedDateTime

@Composable
fun DriveLatestBackupCard(
    modifiedTime: Long?,
    modifier: Modifier = Modifier
) {
    PrimaryCard(modifier = modifier) {
        SettingItem(
            title = stringResource(R.string.msg_latest_backup),
            subtitle = if (modifiedTime != null) {
                modifiedTime.formattedDateTime()
            } else {
                stringResource(R.string.msg_no_online_backup_yet)
            },
            icon = Icons.Rounded.Schedule,
            trailing = {}
        )
    }
}
