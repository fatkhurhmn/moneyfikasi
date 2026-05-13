package dev.muffar.moneyfikasi.more

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.more.component.SettingsItem
import dev.muffar.moneyfikasi.resource.R

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackupRestoreClick: () -> Unit,
    onExportClick: () -> Unit,
    onAppLockClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.settings_menu),
                onBackClick = onBackClick
            )
        }
    ) {
        Column(
            modifier = modifier.padding(it)
        ) {
            SettingsItem(
                title = stringResource(R.string.backup_restore),
                icon = painterResource(id = R.drawable.ic_backup_restore),
                onClick = onBackupRestoreClick
            )
            SettingsItem(
                title = stringResource(R.string.export),
                icon = painterResource(id = R.drawable.ic_export),
                onClick = onExportClick
            )
            SettingsItem(
                title = stringResource(R.string.app_lock),
                icon = painterResource(id = R.drawable.ic_applock),
                onClick = onAppLockClick
            )
        }
    }
}
