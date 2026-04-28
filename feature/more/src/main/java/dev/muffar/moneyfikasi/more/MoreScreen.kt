package dev.muffar.moneyfikasi.more

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.more.component.MoreItem
import dev.muffar.moneyfikasi.more.component.MoreTopBar

@Composable
fun MoreScreen(
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
            MoreTopBar()
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .padding(vertical = 4.dp)
        ) {
            MoreItem(
                title = stringResource(R.string.wallets),
                icon = painterResource(id = R.drawable.ic_wallet),
                onClick = onWalletsClick
            )
            MoreItem(
                title = stringResource(R.string.categories),
                icon = painterResource(id = R.drawable.ic_category),
                onClick = onCategoriesClick
            )
            MoreItem(
                title = stringResource(R.string.presets),
                icon = painterResource(id = R.drawable.ic_ink),
                onClick = onPresetClick
            )
            MoreItem(
                title = stringResource(R.string.budgets),
                icon = painterResource(id = R.drawable.ic_budget),
                onClick = onBudgetsClick
            )
            MoreItem(
                title = stringResource(R.string.backup_restore),
                icon = painterResource(id = R.drawable.ic_restore_backup),
                onClick = onBackupRestoreClick
            )
            MoreItem(
                title = stringResource(R.string.export),
                icon = painterResource(id = R.drawable.ic_export),
                onClick = onExportClick
            )
            MoreItem(
                title = stringResource(R.string.app_lock),
                icon = painterResource(id = R.drawable.ic_applock),
                onClick = onAppLockClick
            )
        }
    }
}
