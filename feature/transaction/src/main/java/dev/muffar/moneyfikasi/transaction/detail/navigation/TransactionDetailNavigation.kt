package dev.muffar.moneyfikasi.transaction.detail.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.transaction.detail.TransactionDetailScreen
import dev.muffar.moneyfikasi.transaction.detail.TransactionDetailViewModel
import java.util.UUID

fun NavGraphBuilder.transactionDetailNavigation(
    onNavigateBack: () -> Unit,
) {
    composable(
        route = Screen.TransactionDetail.route
    ) {
        val viewModel = hiltViewModel<TransactionDetailViewModel>()
        val state by viewModel.state.collectAsState()

        TransactionDetailScreen(
            state = state,
            onBackClick = onNavigateBack
        )
    }
}

fun NavController.toTransactionDetail(id: UUID) {
    navigate(Screen.TransactionDetail.routeWithArg(id))
}