package dev.muffar.moneyfikasi.statistic.detail.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.statistic.detail.StatisticDetailEvent
import dev.muffar.moneyfikasi.statistic.detail.StatisticDetailScreen
import dev.muffar.moneyfikasi.statistic.detail.StatisticDetailViewModel
import java.util.UUID

fun NavGraphBuilder.statisticDetailNavigation(
    transactions: List<Transaction>,
    onNavigateToDetail: (UUID) -> Unit,
    onNavigateBack: () -> Unit,
) {
    composable(Screen.StatisticDetail.route) {
        val viewModel = hiltViewModel<StatisticDetailViewModel>()
        val state by viewModel.state.collectAsState()
        val event = viewModel::onEvent

        LaunchedEffect(Unit) {
            event(StatisticDetailEvent.OnInitState(transactions, transactions.first().type))
        }

        StatisticDetailScreen(
            state = state,
            onClick = onNavigateToDetail,
            onBackClick = onNavigateBack
        )
    }
}

fun NavController.toStatisticDetailScreen(transactions: List<Transaction>) {
    currentBackStackEntry?.savedStateHandle?.set(
        Screen.StatisticDetail.TRANSACTIONS,
        transactions
    )
    navigate(Screen.StatisticDetail.route)
}