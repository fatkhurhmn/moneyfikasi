package dev.muffar.moneyfikasi.feature.dashboard.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.feature.dashboard.DashboardEvent
import dev.muffar.moneyfikasi.feature.dashboard.DashboardScreen
import dev.muffar.moneyfikasi.feature.dashboard.DashboardViewModel
import dev.muffar.moneyfikasi.navigation.Screen

fun NavGraphBuilder.dashboardNavigation() {
    composable(Screen.Dashboard.route) {
        val viewModel = hiltViewModel<DashboardViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        DashboardScreen(
            state = state,
            onShowReportDateSheet = { viewModel.onEvent(DashboardEvent.ShowReportDateSheet(it)) },
            onDateRangeChange = { viewModel.onEvent(DashboardEvent.DateRangeChanged(it)) },
            onToggleBalanceVisibility = { viewModel.onEvent(DashboardEvent.ToggleBalanceVisibility) }
        )
    }
}