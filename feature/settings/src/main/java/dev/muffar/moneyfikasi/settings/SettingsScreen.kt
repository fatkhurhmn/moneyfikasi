package dev.muffar.moneyfikasi.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.settings.component.SettingsItem
import dev.muffar.moneyfikasi.settings.component.SettingsTopBar

@Composable
fun SettingsScreen(
    modifier : Modifier = Modifier,
    onWalletsClick : () -> Unit,
    onCategoriesClick : () -> Unit,
    onBackupRestoreClick : () -> Unit
) {
    Scaffold(
        topBar = {
            SettingsTopBar(modifier = Modifier.padding(16.dp))
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
                title = stringResource(R.string.backup_restore),
                icon = painterResource(id = R.drawable.ic_restore_backup),
                onClick = onBackupRestoreClick
            )
        }
    }
}