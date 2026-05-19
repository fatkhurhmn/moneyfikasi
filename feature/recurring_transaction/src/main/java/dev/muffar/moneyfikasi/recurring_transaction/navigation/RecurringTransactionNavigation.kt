package dev.muffar.moneyfikasi.recurring_transaction.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.recurring_transaction.add_edit.AddEditRecurringTransactionScreen
import dev.muffar.moneyfikasi.recurring_transaction.list.RecurringTransactionsScreen
import dev.muffar.moneyfikasi.recurring_transaction.list.RecurringTransactionsViewModel
import java.util.UUID

fun NavController.toRecurringTransactionsScreen() {
    navigate(Screen.RecurringTransactions.route)
}

fun NavController.toAddEditRecurringTransactionScreen(id: UUID? = null) {
    navigate(Screen.AddEditRecurringTransaction.routeWithArg(id))
}

fun NavGraphBuilder.recurringTransactionNavGraph(
    navigateToAddRecurringTransaction: () -> Unit,
    navigateToEditRecurringTransaction: (UUID) -> Unit,
    navigateBack: () -> Unit
) {
    composable(route = Screen.RecurringTransactions.route) {
        val viewModel = hiltViewModel<RecurringTransactionsViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        RecurringTransactionsScreen(
            state = state,
            onAddRecurringTransactionClick = navigateToAddRecurringTransaction,
            onRecurringTransactionClick = navigateToEditRecurringTransaction,
            onBackClick = navigateBack
        )
    }

    composable(route = Screen.AddEditRecurringTransaction.route) {
        AddEditRecurringTransactionScreen(
            onBackClick = navigateBack
        )
    }
}
