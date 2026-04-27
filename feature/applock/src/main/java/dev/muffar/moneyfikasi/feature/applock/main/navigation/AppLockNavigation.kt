package dev.muffar.moneyfikasi.feature.applock.main.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.domain.model.EnterPinType
import dev.muffar.moneyfikasi.feature.applock.main.AppLockEvent
import dev.muffar.moneyfikasi.feature.applock.main.AppLockScreen
import dev.muffar.moneyfikasi.feature.applock.main.AppLockViewModel
import dev.muffar.moneyfikasi.navigation.Screen

fun NavGraphBuilder.appLockNavigation(
    navigateBack: () -> Unit,
    onNavigateToEnterPin: (EnterPinType) -> Unit,
) {
    composable(route = Screen.AppLock.route) {
        val viewModel = hiltViewModel<AppLockViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val eventFlow = viewModel.eventFlow
        val onEvent = viewModel::onEvent

        AppLockScreen(
            state = state,
            eventFlow = eventFlow,
            onAppLockEnabledChanged = { onEvent(AppLockEvent.OnAppLockEnabledChanged(it)) },
            onBackClick = navigateBack,
            onNavigateToEnterPin = { onNavigateToEnterPin(it) }
        )
    }
}

fun NavController.toAppLockScreen() {
    navigate(Screen.AppLock.route)
}
