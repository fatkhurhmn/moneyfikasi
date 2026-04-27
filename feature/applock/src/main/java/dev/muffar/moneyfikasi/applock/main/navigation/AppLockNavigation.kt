package dev.muffar.moneyfikasi.applock.main.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.applock.main.AppLockEvent
import dev.muffar.moneyfikasi.applock.main.AppLockScreen
import dev.muffar.moneyfikasi.applock.main.AppLockViewModel
import dev.muffar.moneyfikasi.navigation.Screen

fun NavGraphBuilder.appLockNavGraph(
    navigateBack: () -> Unit,
) {
    composable(route = Screen.AppLock.route) {
        val viewModel = hiltViewModel<AppLockViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val onEvent = viewModel::onEvent

        AppLockScreen(
            state = state,
            onAppLockEnabledChanged = { onEvent(AppLockEvent.OnAppLockEnabledChanged(it)) },
            onPinChanged = { onEvent(AppLockEvent.OnPinChanged(it)) },
            onConfirmPinChanged = { onEvent(AppLockEvent.OnConfirmPinChanged(it)) },
            onSaveAppLock = { onEvent(AppLockEvent.OnSaveAppLock) },
            onBackClick = navigateBack
        )
    }
}

fun NavController.toAppLockScreen() {
    navigate(Screen.AppLock.route)
}
