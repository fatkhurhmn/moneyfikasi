package dev.muffar.moneyfikasi.preset

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.preset.add_edit.navigation.addEditPresetNavigation
import dev.muffar.moneyfikasi.preset.list.navigation.presetsNavigation
import java.util.UUID

fun NavGraphBuilder.presetGraph(
    navigateToAddPreset: (TransactionType) -> Unit,
    navigateToEditPreset: (TransactionType, UUID) -> Unit,
    navigateToAddWallet: () -> Unit,
    navigateToAddCategory: (type: CategoryType) -> Unit,
    navigateBack: () -> Unit
) {
    presetsNavigation(
        navigateToAddPreset = navigateToAddPreset,
        navigateToEditPreset = navigateToEditPreset,
        navigateBack = navigateBack
    )

    addEditPresetNavigation(
        navigateBack = navigateBack,
        navigateToAddWallet = navigateToAddWallet,
        navigateToAddCategory = navigateToAddCategory
    )
}