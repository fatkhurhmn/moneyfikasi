package dev.muffar.moneyfikasi.preset

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.preset.add_edit.navigation.addEditPresetNavigation
import dev.muffar.moneyfikasi.preset.list.navigation.presetListNavigation
import java.util.UUID

fun NavGraphBuilder.presetGraph(
    onAddPresetClick: () -> Unit,
    onPresetClick: (UUID) -> Unit,
    navigateBack: () -> Unit
) {
    presetListNavigation(
        onAddPresetClick = onAddPresetClick,
        onPresetClick = onPresetClick,
        navigateBack = navigateBack
    )

    addEditPresetNavigation(
        navigateBack = navigateBack
    )
}