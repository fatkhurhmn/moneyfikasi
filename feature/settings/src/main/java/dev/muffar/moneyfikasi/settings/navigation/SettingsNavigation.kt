package dev.muffar.moneyfikasi.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.settings.SettingsScreen

fun NavGraphBuilder.settingsNavGraph(
    navigateToWallets: () -> Unit,
    navigateToCategories: () -> Unit,
    navigateToPreset: () -> Unit,
    navigateToBackupRestore: () -> Unit,
) {
    composable(route = Screen.Settings.route) {
        SettingsScreen(
            onWalletsClick = navigateToWallets,
            onCategoriesClick = navigateToCategories,
            onPresetClick = navigateToPreset,
            onBackupRestoreClick = navigateToBackupRestore
        )
    }
}