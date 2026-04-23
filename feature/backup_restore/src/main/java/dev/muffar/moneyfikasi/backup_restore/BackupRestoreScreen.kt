package dev.muffar.moneyfikasi.backup_restore

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.backup_restore.component.BackupRestoreImage
import dev.muffar.moneyfikasi.backup_restore.component.BackupRestoreNotes
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.button.CommonButton
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BackupRestoreScreen(
    modifier: Modifier = Modifier,
    eventFlow: SharedFlow<BackupRestoreViewModel.UiEvent>,
    onBackupClick: (Uri) -> Unit,
    onRestoreClick: (Uri) -> Unit,
    onBackClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val dirBackupLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
            if (it != null) {
                onBackupClick(it)
            }
        }

    val dirRestoreLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
            if (it != null) {
                onRestoreClick(it)
            }
        }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.backup_restore),
                onBackClick = onBackClick
            )
        },
        snackbarHost = { SnackbarMessage(snackbarHostState) }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .padding(vertical = 4.dp)
        ) {
            BackupRestoreImage()
            CommonButton(
                text = stringResource(R.string.backup),
                onClick = { dirBackupLauncher.launch(null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            CommonButton(
                text = stringResource(R.string.restore),
                onClick = { dirRestoreLauncher.launch(arrayOf("application/zip")) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            BackupRestoreNotes()
        }
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is BackupRestoreViewModel.UiEvent.ShowMessage -> {
                    snackbarHostState.showMessage(it.message, it.type)
                }
            }
        }
    }
}