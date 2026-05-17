package dev.muffar.moneyfikasi.backup_restore.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingItem
import dev.muffar.moneyfikasi.resource.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LatestBackupInfo(
    fileName: String,
    date: Long,
) {
    val subtitle = if (fileName.isNotEmpty()) {
        SimpleDateFormat(
            "dd MMM yyyy, HH:mm",
            Locale.getDefault()
        ).format(Date(date))
    } else {
        stringResource(R.string.no_backup_yet)
    }

    PrimaryCard {
        SettingItem(
            title = stringResource(R.string.latest_backup),
            subtitle = subtitle,
            icon = Icons.Rounded.Schedule,
            trailing = {}
        )
    }
}
