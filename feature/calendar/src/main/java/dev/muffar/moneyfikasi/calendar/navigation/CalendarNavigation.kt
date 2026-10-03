package dev.muffar.moneyfikasi.calendar.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.calendar.CalendarScreen
import dev.muffar.moneyfikasi.calendar.CalendarViewModel
import dev.muffar.moneyfikasi.navigation.Screen
import java.util.UUID

fun NavController.navigateToCalendar() {
    navigate(Screen.Calendar.route)
}

fun NavGraphBuilder.calendarNavGraph(
    onTransactionClick: (UUID, Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    composable(route = Screen.Calendar.route) {
        val viewModel = hiltViewModel<CalendarViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        CalendarScreen(
            state = state,
            onPreviousMonth = viewModel::onPreviousMonth,
            onNextMonth = viewModel::onNextMonth,
            onDaySelected = viewModel::onDaySelected,
            onTransactionClick = onTransactionClick,
            onBackClick = onBackClick
        )
    }
}
