package dev.muffar.moneyfikasi.backup_restore.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonButton
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonOutlinedButton
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.dialog.CommonAlertDialog
import dev.muffar.moneyfikasi.resource.R

@Composable
fun DriveBackupButtonsCard(
    isSignedIn: Boolean,
    isBusy: Boolean,
    canRestore: Boolean,
    onSignInClick: () -> Unit,
    onBackupClick: () -> Unit,
    onRestoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showRestoreDialog by remember { mutableStateOf(false) }

    PrimaryCard(modifier = modifier) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            DriveBackupText()
            Spacer(modifier = Modifier.height(12.dp))
            if (!isSignedIn) {
                CommonButton(
                    text = stringResource(R.string.action_connect_drive),
                    onClick = onSignInClick,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CommonButton(
                        text = stringResource(R.string.action_backup),
                        onClick = onBackupClick,
                        modifier = Modifier.weight(1f),
                        enabled = !isBusy
                    )
                    CommonOutlinedButton(
                        text = stringResource(R.string.action_restore),
                        onClick = { showRestoreDialog = true },
                        modifier = Modifier.weight(1f),
                        enabled = !isBusy && canRestore
                    )
                }
            }
        }
    }

    if (showRestoreDialog) {
        CommonAlertDialog(
            title = stringResource(R.string.title_restore_confirmation),
            message = stringResource(R.string.msg_restore_confirmation),
            positiveText = stringResource(R.string.action_restore),
            negativeText = stringResource(R.string.action_cancel),
            onDismiss = { showRestoreDialog = false },
            onConfirm = {
                onRestoreClick()
                showRestoreDialog = false
            }
        )
    }
}
