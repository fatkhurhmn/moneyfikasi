package dev.muffar.moneyfikasi.debt.list.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.muffar.moneyfikasi.debt.list.DebtsEvent
import dev.muffar.moneyfikasi.debt.list.DebtsScreen
import dev.muffar.moneyfikasi.debt.list.DebtsViewModel
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.navigation.Screen
import java.util.UUID

fun NavGraphBuilder.debtsNavigation(
    navigateToAddDebt: (DebtType) -> Unit,
    navigateToEditDebt: (DebtType, UUID) -> Unit,
    navigateToTransactionDetail: (UUID) -> Unit,
    navigateBack: () -> Unit,
) {
    composable(
        route = Screen.Debts.route,
        arguments = listOf(
            navArgument(Screen.Debts.TYPE) { type = NavType.StringType }
        )
    ) {
        val viewModel = hiltViewModel<DebtsViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        DebtsScreen(
            state = state,
            onDebtClick = { id ->
                state.debts.find { it.id == id }?.let {
                    val transactionId = it.transactionId
                    if (transactionId != null) {
                        navigateToTransactionDetail(transactionId)
                    } else {
                        navigateToEditDebt(it.type, it.id)
                    }
                }
            },
            onAddDebtClick = navigateToAddDebt,
            onStatusFilterChange = { viewModel.onEvent(DebtsEvent.StatusFilterChanged(it)) },
            onBackClick = navigateBack
        )
    }
}

fun NavController.toDebtsScreen(type: DebtType) {
    this.navigate(Screen.Debts.routeWithArg(type))
}
