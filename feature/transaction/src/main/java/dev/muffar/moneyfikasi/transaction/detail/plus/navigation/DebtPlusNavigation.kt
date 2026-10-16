package dev.muffar.moneyfikasi.transaction.detail.plus.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.transaction.detail.plus.DebtPlusEvent
import dev.muffar.moneyfikasi.transaction.detail.plus.DebtPlusScreen
import dev.muffar.moneyfikasi.transaction.detail.plus.DebtPlusViewModel
import java.util.UUID

fun NavGraphBuilder.debtPlusNavigation(
    navigateBack: () -> Unit,
    onNavigateToAddWallet: () -> Unit,
) {
    composable(
        route = Screen.DebtPlus.route,
        arguments = listOf(
            navArgument(Screen.DebtPlus.DEBT_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(Screen.DebtPlus.TAB) {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(Screen.DebtPlus.HISTORY_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
        val viewModel = hiltViewModel<DebtPlusViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        DebtPlusScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onTabSelected = { viewModel.onEvent(DebtPlusEvent.TabSelected(it)) },
            onAmountChange = { viewModel.onEvent(DebtPlusEvent.AmountChanged(it)) },
            onWalletSelect = { viewModel.onEvent(DebtPlusEvent.WalletSelected(it)) },
            onAddNewWalletClick = onNavigateToAddWallet,
            onDateSelect = { viewModel.onEvent(DebtPlusEvent.DateSelected(it)) },
            onSubmit = { viewModel.onEvent(DebtPlusEvent.SavePlus) },
            onShowDeleteAlert = { viewModel.onEvent(DebtPlusEvent.ShowDeleteAlert(it)) },
            onDelete = { viewModel.onEvent(DebtPlusEvent.DeleteHistory) },
            onBackClick = navigateBack
        )
    }
}

fun NavController.toDebtPlusScreen(debtId: UUID, tab: Int = 0, historyId: UUID? = null) {
    this.navigate(Screen.DebtPlus.routeWithArg(debtId, tab, historyId))
}
