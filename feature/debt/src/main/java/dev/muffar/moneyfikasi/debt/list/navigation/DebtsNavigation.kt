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
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.navigation.Screen
import java.util.UUID

fun NavGraphBuilder.debtsNavigation(
    navigateToAddDebt: (DebtType) -> Unit,
    navigateToEditDebt: (DebtType, UUID) -> Unit,
    navigateBack: () -> Unit,
) {
    composable(route = Screen.Debts.route) {
        val viewModel = hiltViewModel<DebtsViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        DebtsScreen(
            state = state,
            onDebtClick = { id ->
                state.debts.find { it.id == id }?.let {
                    navigateToEditDebt(it.type, it.id)
                }
            },
            onAddDebtClick = navigateToAddDebt,
            onFilterChange = { viewModel.onEvent(DebtsEvent.FilterChanged(it)) },
            onBackClick = navigateBack
        )
    }
}

fun NavController.toDebtsScreen() {
    this.navigate(Screen.Debts.route)
}
