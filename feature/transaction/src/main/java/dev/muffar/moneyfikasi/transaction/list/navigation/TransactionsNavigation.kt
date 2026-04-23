package dev.muffar.moneyfikasi.transaction.list.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.transaction.list.TransactionsEvent
import dev.muffar.moneyfikasi.transaction.list.TransactionsScreen
import dev.muffar.moneyfikasi.transaction.list.TransactionsViewModel
import java.util.UUID

fun NavGraphBuilder.transactionsNavigation(
    onNavigateToTransactionDetail: (UUID, Boolean) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    composable(Screen.Transactions.route) {
        val viewModel = hiltViewModel<TransactionsViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        TransactionsScreen(
            state = state,
            onTransactionItemClick = onNavigateToTransactionDetail,
            onTimeReferenceChange = { event(TransactionsEvent.TimeReferenceChanged(it)) },
            onDateRangeChange = { event(TransactionsEvent.DateRangeChanged(it)) },
            onShowFilterSheet = { event(TransactionsEvent.ShowFilterSheet(it)) },
            onShowChooseDateSheet = { event(TransactionsEvent.ShowChooseDateSheet(it)) },
            onShowCustomDateSheet = { event(TransactionsEvent.ShowCustomDateSheet(it)) },
            onSearchClick = onNavigateToSearch,
            onResetFilter = { event(TransactionsEvent.ResetFilter) },
            onFilterChanged = { event(TransactionsEvent.FilterChanged(it)) },
            onGetDailyBalance = viewModel::getDailyBalance
        )
    }
}