package dev.muffar.moneyfikasi.debt.list.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.debt.list.DebtsEvent
import dev.muffar.moneyfikasi.debt.list.DebtsScreen
import dev.muffar.moneyfikasi.debt.list.DebtsViewModel
import dev.muffar.moneyfikasi.navigation.Screen

fun NavGraphBuilder.debtsNavigation(
    navigateToAddDebt: (dev.muffar.moneyfikasi.domain.model.DebtType) -> Unit,
    navigateToEditDebt: (dev.muffar.moneyfikasi.domain.model.DebtType, java.util.UUID) -> Unit,
    navigateBack: () -> Unit,
) {
    composable(route = Screen.Debts.route) {
        val viewModel = hiltViewModel<DebtsViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        DebtsScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onDebtClick = { id ->
                state.debts.find { it.id == id }?.let {
                    navigateToEditDebt(it.type, it.id)
                }
            },
            onDebtPayClick = { viewModel.onEvent(DebtsEvent.PaymentOpened(it)) },
            onAddDebtClick = navigateToAddDebt,
            onShowAddSheet = { viewModel.onEvent(DebtsEvent.ShowAddSheetChanged(it)) },
            onPaymentAmountChange = { viewModel.onEvent(DebtsEvent.PaymentAmountChanged(it)) },
            onPaymentDismiss = { viewModel.onEvent(DebtsEvent.PaymentDismissed) },
            onPaymentConfirm = { viewModel.onEvent(DebtsEvent.PaymentConfirmed) },
            onFilterChange = { viewModel.onEvent(DebtsEvent.FilterChanged(it)) },
            onBackClick = navigateBack
        )
    }
}

fun NavController.toDebtsScreen() {
    this.navigate(Screen.Debts.route)
}
