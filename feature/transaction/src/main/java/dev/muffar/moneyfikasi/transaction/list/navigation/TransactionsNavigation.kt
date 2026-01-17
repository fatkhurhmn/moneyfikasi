package dev.muffar.moneyfikasi.transaction.list.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.transaction.list.TransactionsEvent
import dev.muffar.moneyfikasi.transaction.list.TransactionsScreen
import dev.muffar.moneyfikasi.transaction.list.TransactionsViewModel
import java.util.UUID

fun NavGraphBuilder.transactionsNavigation(
    onNavigateToTransactionDetail: (UUID) -> Unit,
    onNavigateToAddScreen: (TransactionType) -> Unit,
    onNavigateToTransferScreen: () -> Unit
) {
    composable(Screen.Transactions.route) {
        val viewModel = hiltViewModel<TransactionsViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        TransactionsScreen(
            state = state,
            onTransactionItemClick = onNavigateToTransactionDetail,
            onFloatingActionButtonClick = { event(TransactionsEvent.FloatingActionButtonClicked(it)) },
            onAddTransactionClick = {
                if (it != null) {
                    onNavigateToAddScreen(it)
                } else {
                    onNavigateToTransferScreen()
                }
            },
            onFilterChange = { event(TransactionsEvent.FilterChanged(it)) },
            onLocalDateTimeChange = { event(TransactionsEvent.LocalDateTimeChanged(it)) },
            onDateRangeChange = { start, end ->
                event(TransactionsEvent.DateRangeChanged(start, end))
            },
            onShowFilterSheet = { event(TransactionsEvent.ShowFilterSheet(it)) },
            onFilterCategories = { event(TransactionsEvent.CategoryFiltered(it)) },
            onFilterWallets = { event(TransactionsEvent.WalletFiltered(it)) },
            onVisibilityClick = { event(TransactionsEvent.VisibilityClicked) },
            onApplyFilter = { event(TransactionsEvent.ApplyFilter) }
        )
    }
}