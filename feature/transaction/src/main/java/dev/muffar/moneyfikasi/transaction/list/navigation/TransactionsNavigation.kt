package dev.muffar.moneyfikasi.transaction.list.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.transaction.list.TransactionsEvent
import dev.muffar.moneyfikasi.transaction.list.TransactionsScreen
import dev.muffar.moneyfikasi.transaction.list.TransactionsViewModel
import java.util.UUID

fun NavGraphBuilder.transactionsNavigation(
    onNavigateToTransactionDetail: (UUID) -> Unit,
) {
    composable(Screen.Transactions.route) {
        val viewModel = hiltViewModel<TransactionsViewModel>()
        val state by viewModel.state.collectAsState()
        val event = viewModel::onEvent

        TransactionsScreen(
            modifier = Modifier,
            state = state,
            onTransactionItemClick = onNavigateToTransactionDetail,
            onFilterChanged = { event(TransactionsEvent.OnFilterChanged(it)) },
            onDateRangeChange = { start, end ->
                event(TransactionsEvent.OnDateRangeChanged(start, end))
            },
            onShowBottomSheet = { event(TransactionsEvent.OnShowBottomSheet(it)) },
        )
    }
}