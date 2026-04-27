package dev.muffar.moneyfikasi.feature.applock

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.feature.applock.main.navigation.appLockNavigation
import dev.muffar.moneyfikasi.feature.applock.enter_pin.navigation.enterPinNavGraph

fun NavGraphBuilder.appLockNavGraph(
    navigateBack: () -> Unit,
    onNavigateToEnterPin: () -> Unit,
) {
    appLockNavigation(
        navigateBack = navigateBack,
        onNavigateToEnterPin = onNavigateToEnterPin
    )
    enterPinNavGraph(
        navigateBack = navigateBack
    )
}
