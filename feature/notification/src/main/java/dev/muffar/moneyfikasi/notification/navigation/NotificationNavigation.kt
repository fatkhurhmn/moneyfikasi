package dev.muffar.moneyfikasi.notification.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.notification.NotificationScreen
import dev.muffar.moneyfikasi.notification.NotificationViewModel

fun NavController.navigateToNotification() {
    navigate(Screen.Notifications.route)
}

fun NavGraphBuilder.notificationNavGraph(
    onBackClick: () -> Unit,
) {
    composable(route = Screen.Notifications.route) {
        val viewModel: NotificationViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        NotificationScreen(
            state = state,
            onEvent = viewModel::onEvent,
            onBackClick = onBackClick
        )
    }
}
