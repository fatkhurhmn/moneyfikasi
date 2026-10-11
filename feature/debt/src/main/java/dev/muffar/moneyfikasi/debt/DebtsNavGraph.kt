package dev.muffar.moneyfikasi.debt

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.debt.add_edit.navigation.addEditDebtNavigation
import dev.muffar.moneyfikasi.debt.list.navigation.debtsNavigation
import java.util.UUID

fun NavGraphBuilder.debtsNavGraph(
    navigateToAddDebt: () -> Unit,
    navigateToEditDebt: (UUID) -> Unit,
    navigateBack: () -> Unit,
) {
    debtsNavigation(
        navigateToAddDebt = navigateToAddDebt,
        navigateToEditDebt = navigateToEditDebt,
        navigateBack = navigateBack,
    )
    addEditDebtNavigation(
        navigateBack = navigateBack,
    )
}
