package dev.muffar.moneyfikasi.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.settings.component.SettingsItem
import dev.muffar.moneyfikasi.settings.component.SettingsTopBar

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onWalletsClick: () -> Unit,
    onCategoriesClick: () -> Unit,
    onPresetClick: () -> Unit,
    onBudgetsClick: () -> Unit,
    onBackupRestoreClick: () -> Unit,
    onExportClick: () -> Unit,
    onAppLockClick: () -> Unit
) {
    Scaffold(
        topBar = {
            SettingsTopBar()
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .padding(vertical = 4.dp)
        ) {
            SettingsItem(
                title = stringResource(R.string.wallets),
                icon = painterResource(id = R.drawable.ic_wallet),
                onClick = onWalletsClick
            )
            SettingsItem(
                title = stringResource(R.string.categories),
                icon = painterResource(id = R.drawable.ic_category),
                onClick = onCategoriesClick
            )
            SettingsItem(
                title = stringResource(R.string.presets),
                icon = painterResource(id = R.drawable.ic_ink),
                onClick = onPresetClick
            )
            SettingsItem(
                title = stringResource(R.string.budgets),
                icon = painterResource(id = R.drawable.ic_budget),
                onClick = onBudgetsClick
            )
            SettingsItem(
                title = stringResource(R.string.backup_restore),
                icon = painterResource(id = R.drawable.ic_restore_backup),
                onClick = onBackupRestoreClick
            )
            SettingsItem(
                title = stringResource(R.string.export),
                icon = painterResource(id = R.drawable.ic_export),
                onClick = onExportClick
            )
            SettingsItem(
                title = stringResource(R.string.app_lock),
                icon = rememberVectorPainter(Icons.Rounded.Lock),
                onClick = onAppLockClick
            )
        }
    }
}
