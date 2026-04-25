package dev.muffar.moneyfikasi.backup_restore

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.backup_restore.component.AutoBackupSection
import dev.muffar.moneyfikasi.backup_restore.component.BackupLatestInfo
import dev.muffar.moneyfikasi.backup_restore.component.BackupRestoreButton
import dev.muffar.moneyfikasi.backup_restore.component.BackupRestoreImage
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.dialog.LoadingDialog
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            BackupRestoreImage()

            BackupRestoreButton(
                state = state,
                onBackupClick = onBackupClick,
                onRestoreClick = onRestoreClick
            )

            BackupLatestInfo(
                fileName = state.latestBackupName,
                date = state.latestBackupDate
            )

            AutoBackupSection(
                isEnabled = state.isAutoBackupEnabled,
                onEnabledChange = onAutoBackupEnabledChange,
                folderUri = state.autoBackupUri,
                onFolderSelected = onAutoBackupFolderSelected,
                period = state.autoBackupPeriod,
                onPeriodSelected = onAutoBackupPeriodSelected
            )
        }
    }

    if (state.isLoading) {
        LoadingDialog(message = stringResource(R.string.loading))
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
