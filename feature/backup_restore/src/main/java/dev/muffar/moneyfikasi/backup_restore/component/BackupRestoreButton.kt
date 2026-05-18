package dev.muffar.moneyfikasi.backup_restore.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.backup_restore.BackupRestoreState
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonButton
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonOutlinedButton
import dev.muffar.moneyfikasi.common_ui.component.dialog.CommonAlertDialog
import dev.muffar.moneyfikasi.resource.R

@Composable
fun BackupRestoreButton(
    state: BackupRestoreState,
    onBackupClick: (Uri) -> Unit,
    onRestoreClick: (Uri) -> Unit
) {
    var showRestoreDialog by remember { mutableStateOf(false) }
    var selectedRestoreUri by remember { mutableStateOf<Uri?>(null) }

    val dirBackupLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
            if (it != null) {
                onBackupClick(it)
            }
        }

    val dirRestoreLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
            if (it != null) {
                selectedRestoreUri = it
                showRestoreDialog = true
            }
        }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CommonButton(
            text = stringResource(R.string.backup),
            onClick = { dirBackupLauncher.launch(null) },
            modifier = Modifier.weight(1f),
            enabled = !state.isLoading
        )
        CommonOutlinedButton(
            text = stringResource(R.string.restore),
            onClick = { dirRestoreLauncher.launch(arrayOf("application/zip")) },
            modifier = Modifier.weight(1f),
            enabled = !state.isLoading
        )
    }

    if (showRestoreDialog) {
        CommonAlertDialog(
            title = stringResource(R.string.restore_confirmation_title),
            message = stringResource(R.string.restore_confirmation_message),
            positiveText = stringResource(R.string.restore),
            negativeText = stringResource(R.string.cancel),
            onDismiss = {
                showRestoreDialog = false
                selectedRestoreUri = null
            },
            onConfirm = {
                selectedRestoreUri?.let { onRestoreClick(it) }
                showRestoreDialog = false
                selectedRestoreUri = null
            }
        )
    }
}