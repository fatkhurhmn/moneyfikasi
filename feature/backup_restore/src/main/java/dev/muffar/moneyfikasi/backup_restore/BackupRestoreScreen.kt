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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.backup_restore.component.AutoBackupSection
import dev.muffar.moneyfikasi.backup_restore.component.BackupRestoreButton
import dev.muffar.moneyfikasi.backup_restore.component.BackupRestoreText
import dev.muffar.moneyfikasi.backup_restore.component.DeletePreviousBackupSwitch
import dev.muffar.moneyfikasi.backup_restore.component.DriveBackupSection
import dev.muffar.moneyfikasi.backup_restore.component.DriveSectionLabel
import dev.muffar.moneyfikasi.backup_restore.component.LatestBackupInfo
import dev.muffar.moneyfikasi.backup_restore.component.LocalSectionLabel
import dev.muffar.moneyfikasi.common_ui.component.dialog.LoadingDialog
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.TimePeriod
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
    onAutoBackupPeriodSelected: (TimePeriod) -> Unit,
    onDeletePreviousBackupChange: (Boolean) -> Unit,
    onRequestDriveSignIn: () -> Unit,
    onDriveSignOut: () -> Unit,
    onDriveBackup: () -> Unit,
    onDriveRefresh: () -> Unit,
    onDriveRestore: () -> Unit,
    onDriveAutoBackupChange: (Boolean) -> Unit,
    onDriveAutoBackupPeriodSelected: (TimePeriod) -> Unit,
    onBackClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.title_backup_restore),
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            BackupRestoreText()

            LocalSectionLabel()

            BackupRestoreButton(
                state = state,
                onBackupClick = onBackupClick,
                onRestoreClick = onRestoreClick
            )

            DeletePreviousBackupSwitch(
                isEnabled = state.isDeletePreviousBackup,
                onEnabledChange = onDeletePreviousBackupChange
            )

            AutoBackupSection(
                isEnabled = state.autoBackup.isEnabled,
                onEnabledChange = onAutoBackupEnabledChange,
                folderUri = state.autoBackup.uri,
                onFolderSelected = onAutoBackupFolderSelected,
                period = TimePeriod.valueOf(state.autoBackup.period),
                onPeriodSelected = onAutoBackupPeriodSelected,
            )

            LatestBackupInfo(
                fileName = state.latestBackup.name,
                date = state.latestBackup.date,
            )

            DriveSectionLabel(
                isSignedIn = state.isDriveSignedIn,
                isLoading = state.isDriveLoading,
                onRefreshClick = onDriveRefresh
            )

            DriveBackupSection(
                state = state,
                onSignInClick = onRequestDriveSignIn,
                onSignOutClick = onDriveSignOut,
                onBackupClick = onDriveBackup,
                onRestoreClick = onDriveRestore,
                onAutoBackupChange = onDriveAutoBackupChange,
                onAutoBackupPeriodSelected = onDriveAutoBackupPeriodSelected
            )
        }
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is BackupRestoreViewModel.UiEvent.ShowMessage -> {
                    val message = it.formatArg?.let { formatArg ->
                        context.applicationContext.getString(it.messageResId, formatArg)
                    } ?: context.applicationContext.getString(it.messageResId)
                    snackbarHostState.showMessage(message, it.type)
                }
            }
        }
    }
}
