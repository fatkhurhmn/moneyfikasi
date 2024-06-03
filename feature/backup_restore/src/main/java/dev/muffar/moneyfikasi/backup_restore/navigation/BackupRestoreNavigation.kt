package dev.muffar.moneyfikasi.backup_restore.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.backup_restore.BackupRestoreScreen
import dev.muffar.moneyfikasi.navigation.Screen

fun NavGraphBuilder.backupRestoreNavGraph(
    navigateBack: () -> Unit,
) {
    composable(route = Screen.BackupRestore.route) {
        BackupRestoreScreen(
            onBackClick = navigateBack
        )
    }
}

fun NavController.toBackupRestoreScreen() {
    navigate(Screen.BackupRestore.route)
}