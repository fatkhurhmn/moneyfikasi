package dev.muffar.moneyfikasi.backup_restore

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.backup_restore.component.AutoBackupSection
import dev.muffar.moneyfikasi.backup_restore.component.BackupLatestInfo
import dev.muffar.moneyfikasi.backup_restore.component.BackupRestoreImage
import dev.muffar.moneyfikasi.common_ui.component.CommonAlertDialog
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.button.CommonButton
import dev.muffar.moneyfikasi.common_ui.component.button.CommonOutlinedButton
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BackupRestoreScreen(
    modifier: Modifier = Modifier,
    state: BackupRestoreState,
    eventFlow: SharedFlow<BackupRestoreViewModel.UiEvent>,
    onBackupClick: (Uri) -> Unit,
    onRestoreClick: (Uri) -> Unit,
    onAutoBackupEnabledChange: (Boolean) -> Unit,
    onAutoBackupFolderSelected: (Uri) -> Unit,
    onAutoBackupPeriodSelected: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            BackupRestoreImage()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CommonButton(
                    text = stringResource(R.string.backup),
                    onClick = { dirBackupLauncher.launch(null) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                )
                CommonOutlinedButton(
                    text = stringResource(R.string.restore),
                    onClick = { dirRestoreLauncher.launch(arrayOf("application/zip")) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            BackupLatestInfo(
                fileName = state.latestBackupName,
                date = state.latestBackupDate
            )
            Spacer(modifier = Modifier.height(16.dp))
            AutoBackupSection(
                isEnabled = state.isAutoBackupEnabled,
                onEnabledChange = onAutoBackupEnabledChange,
                folderUri = state.autoBackupUri,
                onFolderSelected = onAutoBackupFolderSelected,
                period = state.autoBackupPeriod,
                onPeriodSelected = onAutoBackupPeriodSelected
            )
            Spacer(modifier = Modifier.height(16.dp))
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
