package dev.muffar.moneyfikasi.feature.applock

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.feature.applock.main.navigation.appLockNavigation
import dev.muffar.moneyfikasi.feature.applock.set_pin.navigation.setPinNavigation

fun NavGraphBuilder.appLockNavGraph(
    navigateBack: () -> Unit,
    navigateToSetPin: () -> Unit,
) {
    appLockNavigation(
        navigateBack = navigateBack,
        navigateToSetPin = navigateToSetPin
    )
    setPinNavigation(
        navigateBack = navigateBack
    )
}