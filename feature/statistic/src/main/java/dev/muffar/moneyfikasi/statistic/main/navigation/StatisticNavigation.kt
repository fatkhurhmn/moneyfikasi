package dev.muffar.moneyfikasi.statistic.main.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.statistic.main.StatisticEvent
import dev.muffar.moneyfikasi.statistic.main.StatisticScreen
import dev.muffar.moneyfikasi.statistic.main.StatisticViewModel
import java.util.UUID

fun NavGraphBuilder.statisticNavigation(
    onNavigateToStatisticDetail : (Pair<Long, Long>, UUID) -> Unit
) {
    composable(Screen.Statistic.route) {
        val viewModel = hiltViewModel<StatisticViewModel>()
        val state by viewModel.state.collectAsState()
        val event = viewModel::onEvent

        StatisticScreen(
            modifier = Modifier,
            state = state,
            onFilterChanged = { event(StatisticEvent.OnFilterChanged(it)) },
            onLocalDateTimeChange = { event(StatisticEvent.OnLocalDateTimeChange(it)) },
            onDateRangeChange = { start, end ->
                event(StatisticEvent.OnDateRangeChanged(start, end))
            },
            onShowBottomSheet = { event(StatisticEvent.OnShowBottomSheet(it)) },
            onItemClick = onNavigateToStatisticDetail
        )
    }
}