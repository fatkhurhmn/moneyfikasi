package dev.muffar.moneyfikasi.settings.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.settings.SettingsEvent
import dev.muffar.moneyfikasi.settings.SettingsScreen
import dev.muffar.moneyfikasi.settings.SettingsViewModel

fun NavController.navigateToSettings() {
    navigate(Screen.Settings.route)
}

fun NavGraphBuilder.settingsNavGraph(
    onNotificationClick: () -> Unit,
    onBackupRestoreClick: () -> Unit,
    onExportClick: () -> Unit,
    onAppLockClick: () -> Unit,
    onAboutClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    composable(route = Screen.Settings.route) {
        val viewModel: SettingsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val onEvent = viewModel::onEvent

        SettingsScreen(
            state = state,
            onAppThemeChanged = { onEvent(SettingsEvent.AppThemeChanged(it)) },
            onAppLanguageChanged = { onEvent(SettingsEvent.AppLanguageChanged(it)) },
            onAmountInputTypeChanged = { onEvent(SettingsEvent.AmountInputTypeChanged(it)) },
            onNotificationClick = onNotificationClick,
            onBackupRestoreClick = onBackupRestoreClick,
            onExportClick = onExportClick,
            onAppLockClick = onAppLockClick,
            onAboutClick = onAboutClick,
            onBackClick = onBackClick
        )
    }
}
