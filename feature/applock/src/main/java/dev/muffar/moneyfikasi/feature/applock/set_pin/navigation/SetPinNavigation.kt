package dev.muffar.moneyfikasi.feature.applock.set_pin.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.feature.applock.set_pin.SetPinEvent
import dev.muffar.moneyfikasi.feature.applock.set_pin.SetPinScreen
import dev.muffar.moneyfikasi.feature.applock.set_pin.SetPinViewModel
import dev.muffar.moneyfikasi.navigation.Screen

fun NavGraphBuilder.setPinNavigation(
    navigateBack: () -> Unit,
) {
    composable(route = Screen.SetPin.route) {
        val viewModel = hiltViewModel<SetPinViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val eventFlow = viewModel.eventFlow
        val onEvent = viewModel::onEvent

        SetPinScreen(
            state = state,
            eventFlow = eventFlow,
            onPinChanged = { onEvent(SetPinEvent.OnPinChanged(it)) },
            onBackToEnterPin = { onEvent(SetPinEvent.OnBackToEnterPin) },
            onCancel = { onEvent(SetPinEvent.OnCancel) },
            onNavigateBack = navigateBack
        )
    }
}

fun NavController.toSetPinScreen() {
    navigate(Screen.SetPin.route)
}
