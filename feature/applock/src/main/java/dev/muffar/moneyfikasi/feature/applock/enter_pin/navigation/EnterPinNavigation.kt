package dev.muffar.moneyfikasi.feature.applock.enter_pin.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.muffar.moneyfikasi.domain.model.EnterPinType
import dev.muffar.moneyfikasi.feature.applock.enter_pin.EnterPinEvent
import dev.muffar.moneyfikasi.feature.applock.enter_pin.EnterPinScreen
import dev.muffar.moneyfikasi.feature.applock.enter_pin.EnterPinViewModel
import dev.muffar.moneyfikasi.navigation.Screen

fun NavGraphBuilder.enterPinNavigation(
    navigateBack: () -> Unit,
    onEnterPinSuccess: () -> Unit,
) {
    composable(
        route = Screen.EnterPin.route,
        arguments = listOf(
            navArgument(Screen.EnterPin.TYPE) {
                type = NavType.StringType
            }
        )
    ) {
        val viewModel = hiltViewModel<EnterPinViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val eventFlow = viewModel.eventFlow
        val onEvent = viewModel::onEvent

        EnterPinScreen(
            state = state,
            eventFlow = eventFlow,
            onPinChanged = { onEvent(EnterPinEvent.OnPinChanged(it)) },
            onNavigateBack = navigateBack,
            onEnterPinSuccess = onEnterPinSuccess
        )
    }
}

fun NavController.toEnterPinScreen(type: EnterPinType) {
    navigate(Screen.EnterPin.routeWithArg(type))
}
