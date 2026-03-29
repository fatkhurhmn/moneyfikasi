package dev.muffar.moneyfikasi.quick_transaction.list.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.quick_transaction.list.QuickTransactionsScreen
import dev.muffar.moneyfikasi.quick_transaction.list.QuickTransactionsViewModel

fun NavGraphBuilder.quickTransactionsNavigation(
    navigateBack: () -> Unit,
) {
    composable(route = Screen.QuickTransaction.route) {
        val viewModel = hiltViewModel<QuickTransactionsViewModel>()
        val state by viewModel.state.collectAsState()

        QuickTransactionsScreen(
            state = state,
            onBackClick = navigateBack
        )
    }
}

fun NavController.toQuickTransactionsScreen() {
    navigate(Screen.QuickTransaction.route)
}
