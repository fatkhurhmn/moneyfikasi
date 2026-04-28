package dev.muffar.moneyfikasi.more.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.more.MoreScreen

fun NavGraphBuilder.moreNavGraph(
    navigateToWallets: () -> Unit,
    navigateToCategories: () -> Unit,
    navigateToPreset: () -> Unit,
    navigateToBudgets: () -> Unit,
    navigateToBackupRestore: () -> Unit,
    navigateToExport: () -> Unit,
    navigateToAppLock: () -> Unit,
) {
    composable(route = Screen.More.route) {
        MoreScreen(
            onWalletsClick = navigateToWallets,
            onCategoriesClick = navigateToCategories,
            onPresetClick = navigateToPreset,
            onBudgetsClick = navigateToBudgets,
            onBackupRestoreClick = navigateToBackupRestore,
            onExportClick = navigateToExport,
            onAppLockClick = navigateToAppLock
        )
    }
}
