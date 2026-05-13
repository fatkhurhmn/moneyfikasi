package dev.muffar.moneyfikasi.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.settings.SettingsScreen

fun NavController.navigateToSettings() {
    navigate(Screen.Settings.route)
}

fun NavGraphBuilder.settingsNavGraph(
    onBackupRestoreClick: () -> Unit,
    onExportClick: () -> Unit,
    onAppLockClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    composable(route = Screen.Settings.route) {
        SettingsScreen(
            onBackupRestoreClick = onBackupRestoreClick,
            onExportClick = onExportClick,
            onAppLockClick = onAppLockClick,
            onBackClick = onBackClick
        )
    }
}
