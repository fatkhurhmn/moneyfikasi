package dev.muffar.moneyfikasi.feature.applock.enter_pin.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.feature.applock.enter_pin.EnterPinEvent
import dev.muffar.moneyfikasi.feature.applock.enter_pin.EnterPinScreen
import dev.muffar.moneyfikasi.feature.applock.enter_pin.EnterPinViewModel
import dev.muffar.moneyfikasi.navigation.Screen

fun NavGraphBuilder.enterPinNavGraph(
    navigateBack: () -> Unit,
) {
    composable(route = Screen.EnterPin.route) {
        val viewModel = hiltViewModel<EnterPinViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val eventFlow = viewModel.eventFlow
        val onEvent = viewModel::onEvent

        EnterPinScreen(
            state = state,
            eventFlow = eventFlow,
            onPinChanged = { onEvent(EnterPinEvent.OnPinChanged(it)) },
            onBackToEnterPin = { onEvent(EnterPinEvent.OnBackToEnterPin) },
            onCancel = { onEvent(EnterPinEvent.OnCancel) },
            onNavigateBack = navigateBack
        )
    }
}

fun NavController.toEnterPinScreen() {
    navigate(Screen.EnterPin.route)
}
