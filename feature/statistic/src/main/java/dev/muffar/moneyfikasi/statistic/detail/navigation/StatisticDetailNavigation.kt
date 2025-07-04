package dev.muffar.moneyfikasi.statistic.detail.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.statistic.detail.StatisticDetailEvent
import dev.muffar.moneyfikasi.statistic.detail.StatisticDetailScreen
import dev.muffar.moneyfikasi.statistic.detail.StatisticDetailViewModel
import java.util.UUID

fun NavGraphBuilder.statisticDetailNavigation(
    dateRange: Pair<Long, Long>?,
    categoryId: UUID?,
    onNavigateToDetail: (UUID) -> Unit,
    onNavigateBack: () -> Unit,
) {
    composable(Screen.StatisticDetail.route) {
        val viewModel = hiltViewModel<StatisticDetailViewModel>()
        val state by viewModel.state.collectAsState()
        val event = viewModel::onEvent

        LaunchedEffect(Unit) {
            if (dateRange != null && categoryId != null) {
                event(StatisticDetailEvent.OnInitState(dateRange, categoryId))
            }
        }

        StatisticDetailScreen(
            state = state,
            onClick = onNavigateToDetail,
            onBackClick = onNavigateBack
        )
    }
}

fun NavController.toStatisticDetailScreen(dateRange: Pair<Long, Long>, categoryId: String) {
    currentBackStackEntry?.savedStateHandle?.set(
        Screen.StatisticDetail.DATE_RANGE,
        dateRange
    )
    currentBackStackEntry?.savedStateHandle?.set(
        Screen.StatisticDetail.CATEGORY,
        categoryId
    )
    navigate(Screen.StatisticDetail.route)
}