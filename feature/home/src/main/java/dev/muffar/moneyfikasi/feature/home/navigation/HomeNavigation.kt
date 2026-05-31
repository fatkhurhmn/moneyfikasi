package dev.muffar.moneyfikasi.feature.home.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.feature.home.HomeEvent
import dev.muffar.moneyfikasi.feature.home.HomeScreen
import dev.muffar.moneyfikasi.feature.home.HomeViewModel
import dev.muffar.moneyfikasi.navigation.Screen
import java.util.UUID

fun NavGraphBuilder.homeNavigation(
    onTransactionClick: (UUID, Boolean) -> Unit,
    onSeeAllTransactionsClick: () -> Unit,
    onPresetClick: (TransactionType, UUID) -> Unit,
    onSeeAllBudgetsClick: () -> Unit,
    navigateToAddPreset: () -> Unit,
    navigateToPresets: () -> Unit,
    navigateToAddBudget: () -> Unit,
    navigateToAddWallet: () -> Unit
) {
    composable(Screen.Home.route) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        HomeScreen(
            state = state,
            onShowReportDateSheet = { viewModel.onEvent(HomeEvent.ShowReportDateSheet(it)) },
            onShowCustomDateSheet = { viewModel.onEvent(HomeEvent.ShowCustomDateSheet(it)) },
            onShowDashboardSettingsSheet = {
                viewModel.onEvent(HomeEvent.ShowDashboardSettingsSheet(it))
            },
            onQuickTransactionVisibilityChange = {
                viewModel.onEvent(HomeEvent.QuickTransactionVisibilityChanged(it))
            },
            onBudgetVisibilityChange = {
                viewModel.onEvent(HomeEvent.BudgetVisibilityChanged(it))
            },
            onDateRangeChange = { viewModel.onEvent(HomeEvent.DateRangeChanged(it)) },
            onToggleBalanceVisibility = { viewModel.onEvent(HomeEvent.ToggleBalanceVisibility) },
            onToggleReportVisibility = { viewModel.onEvent(HomeEvent.ToggleReportVisibility) },
            onSeeAllTransactionsClick = onSeeAllTransactionsClick,
            onTransactionClick = onTransactionClick,
            onPresetClick = onPresetClick,
            onAddPresetClick = navigateToAddPreset,
            onPresetsClick = navigateToPresets,
            onSeeAllBudgetsClick = onSeeAllBudgetsClick,
            onAddBudgetClick = navigateToAddBudget,
            onAddWalletClick = navigateToAddWallet
        )
    }
}
