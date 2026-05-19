package dev.muffar.moneyfikasi.more.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.more.MoreScreen
import dev.muffar.moneyfikasi.more.MoreViewModel
import dev.muffar.moneyfikasi.navigation.Screen

fun NavGraphBuilder.moreNavGraph(
    navigateToWallets: () -> Unit,
    navigateToCategories: () -> Unit,
    navigateToPreset: () -> Unit,
    navigateToBudgets: () -> Unit,
    navigateToRecurringTransactions: () -> Unit,
    navigateToSettings: () -> Unit,
) {
    composable(route = Screen.More.route) {

        val viewModel = hiltViewModel<MoreViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        MoreScreen(
            state = state,
            onWalletsClick = navigateToWallets,
            onCategoriesClick = navigateToCategories,
            onBudgetsClick = navigateToBudgets,
            onPresetsClick = navigateToPreset,
            onRecurringTransactionsClick = navigateToRecurringTransactions,
            onSettingsClick = navigateToSettings
        )
    }
}
