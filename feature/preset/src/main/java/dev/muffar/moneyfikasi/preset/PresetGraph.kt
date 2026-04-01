package dev.muffar.moneyfikasi.preset

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.preset.list.navigation.presetListNavigation

fun NavGraphBuilder.presetGraph(
    navigateBack: () -> Unit
) {
    presetListNavigation(
        navigateBack = navigateBack
    )
}