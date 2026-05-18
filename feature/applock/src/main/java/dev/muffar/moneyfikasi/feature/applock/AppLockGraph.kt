package dev.muffar.moneyfikasi.feature.applock

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.domain.model.EnterPinType
import dev.muffar.moneyfikasi.feature.applock.enter_pin.navigation.enterPinNavigation
import dev.muffar.moneyfikasi.feature.applock.main.navigation.appLockNavigation

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
