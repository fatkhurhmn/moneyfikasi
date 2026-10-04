package dev.muffar.moneyfikasi.backup_restore.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.backup_restore.BackupRestoreEvent
import dev.muffar.moneyfikasi.backup_restore.BackupRestoreScreen
import dev.muffar.moneyfikasi.backup_restore.BackupRestoreViewModel
import dev.muffar.moneyfikasi.navigation.Screen

fun NavGraphBuilder.backupRestoreNavGraph(
    navigateBack: () -> Unit,
) {
    composable(route = Screen.BackupRestore.route) {
        val viewModel = hiltViewModel<BackupRestoreViewModel>()
        val state by viewModel.state
        val event = viewModel::onEvent
        val context = androidx.compose.ui.platform.LocalContext.current
        val activity = context as? android.app.Activity

        BackupRestoreScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onBackupClick = { event(BackupRestoreEvent.BackupData(it)) },
            onRestoreClick = { event(BackupRestoreEvent.RestoreData(it)) },
            onAutoBackupEnabledChange = { event(BackupRestoreEvent.AutoBackupEnabledChanged(it)) },
            onAutoBackupFolderSelected = { event(BackupRestoreEvent.AutoBackupUriChanged(it)) },
            onAutoBackupPeriodSelected = { event(BackupRestoreEvent.AutoBackupPeriodChanged(it)) },
            onDeletePreviousBackupChange = { event(BackupRestoreEvent.DeletePreviousBackupChanged(it)) },
            onRequestDriveSignIn = { if (activity != null) viewModel.requestDriveSignIn(activity) else viewModel.requestDriveSignIn() },
            onDriveSignOut = { event(BackupRestoreEvent.DriveSignOut) },
            onDriveBackup = { event(BackupRestoreEvent.DriveBackupNow) },
            onDriveRefresh = { event(BackupRestoreEvent.DriveLoadBackups) },
            onDriveRestore = { event(BackupRestoreEvent.DriveRestore) },
            onDriveAutoBackupChange = { event(BackupRestoreEvent.DriveAutoBackupChanged(it)) },
            onDriveAutoBackupPeriodSelected = { event(BackupRestoreEvent.DriveAutoBackupPeriodChanged(it)) },
            onBackClick = navigateBack,
        )
    }
}

fun NavController.toBackupRestoreScreen() {
    navigate(Screen.BackupRestore.route)
}