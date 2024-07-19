package dev.muffar.moneyfikasi.backup_restore.navigation

import androidx.hilt.navigation.compose.hiltViewModel
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
        val state = viewModel.state
        val event = viewModel::onEvent

        BackupRestoreScreen(
            state = state.value,
            eventFlow = viewModel.eventFlow,
            onBackupClick = { event(BackupRestoreEvent.OnBackupData) },
            onRestoreClick = { event(BackupRestoreEvent.OnRestoreData) },
            onBackClick = navigateBack,
            onShowBackupAlert = { event(BackupRestoreEvent.OnShowBackupAlert(it)) },
            onShowRestoreAlert = { event(BackupRestoreEvent.OnShowRestoreAlert(it)) }
        )
    }
}

fun NavController.toBackupRestoreScreen() {
    navigate(Screen.BackupRestore.route)
}