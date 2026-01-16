package dev.muffar.moneyfikasi.statistic.detail.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.statistic.detail.StatisticDetailScreen
import dev.muffar.moneyfikasi.statistic.detail.StatisticDetailViewModel
import java.util.UUID

fun NavGraphBuilder.statisticDetailNavigation(
    onNavigateToDetail: (UUID) -> Unit,
    onNavigateBack: () -> Unit,
) {
    composable(Screen.StatisticDetail.route) {
        val viewModel = hiltViewModel<StatisticDetailViewModel>()
        val state by viewModel.state.collectAsState()

        StatisticDetailScreen(
            state = state,
            onClick = onNavigateToDetail,
            onBackClick = onNavigateBack
        )
    }
}

fun NavController.toStatisticDetailScreen(dateRange: Pair<Long, Long>, categoryId: String) {
    navigate(Screen.StatisticDetail.routeWithArg(dateRange.first, dateRange.second, categoryId))
}