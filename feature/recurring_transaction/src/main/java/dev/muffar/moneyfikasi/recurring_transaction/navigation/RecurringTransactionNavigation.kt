package dev.muffar.moneyfikasi.recurring_transaction.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen

fun NavController.toRecurringTransactionsScreen() {
    navigate(Screen.RecurringTransactions.route)
}

fun NavGraphBuilder.recurringTransactionNavGraph(
    navigateBack: () -> Unit
) {
    composable(route = Screen.RecurringTransactions.route) {
        // Placeholder for RecurringTransactionsScreen
    }
}
