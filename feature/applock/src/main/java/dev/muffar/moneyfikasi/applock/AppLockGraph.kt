package dev.muffar.moneyfikasi.applock

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.applock.enter_pin.navigation.enterPinNavigation
import dev.muffar.moneyfikasi.applock.main.navigation.appLockNavigation
import dev.muffar.moneyfikasi.domain.model.EnterPinType

fun NavGraphBuilder.appLockNavGraph(
    navigateBack: () -> Unit,
    onNavigateToEnterPin: (EnterPinType) -> Unit,
    onEnterPinSuccess: () -> Unit,
) {
    appLockNavigation(
        navigateBack = navigateBack,
        onNavigateToEnterPin = onNavigateToEnterPin
    )
    enterPinNavigation(
        navigateBack = navigateBack,
        onEnterPinSuccess = onEnterPinSuccess
    )
}
