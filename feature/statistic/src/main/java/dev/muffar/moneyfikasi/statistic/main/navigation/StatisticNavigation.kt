package dev.muffar.moneyfikasi.statistic.main.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.statistic.main.StatisticEvent
import dev.muffar.moneyfikasi.statistic.main.StatisticScreen
import dev.muffar.moneyfikasi.statistic.main.StatisticViewModel
import java.util.UUID

fun NavGraphBuilder.statisticNavigation(
    onNavigateToAllCategoryStatistic: (Long, Long) -> Unit,
    onNavigateToStatisticDetail: (Pair<Long, Long>, UUID, String) -> Unit
) {
    composable(Screen.Statistic.route) {
        val viewModel = hiltViewModel<StatisticViewModel>()
        val state by viewModel.state.collectAsState()
        val event = viewModel::onEvent

        StatisticScreen(
            modifier = Modifier,
            state = state,
            onTimeReferenceChange = { event(StatisticEvent.TimeReferenceChanged(it)) },
            onDateRangeChange = { event(StatisticEvent.DateRangeChanged(it)) },
            onShowChooseDateSheet = { event(StatisticEvent.ShowChooseDateSheet(it)) },
            onShowCustomDateSheet = { event(StatisticEvent.ShowCustomDateSheet(it)) },
            onItemClick = onNavigateToStatisticDetail,
            onShowAllClick = {
                onNavigateToAllCategoryStatistic(state.dateRange.start, state.dateRange.end)
            }
        )
    }
}