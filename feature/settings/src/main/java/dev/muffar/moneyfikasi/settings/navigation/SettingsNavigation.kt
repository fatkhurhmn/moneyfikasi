package dev.muffar.moneyfikasi.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.settings.SettingsScreen
import dev.muffar.moneyfikasi.settings.SettingsViewModel

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
        val viewModel: SettingsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        SettingsScreen(
            state = state,
            onEvent = viewModel::onEvent,
            onBackupRestoreClick = onBackupRestoreClick,
            onExportClick = onExportClick,
            onAppLockClick = onAppLockClick,
            onBackClick = onBackClick
        )
    }
}
