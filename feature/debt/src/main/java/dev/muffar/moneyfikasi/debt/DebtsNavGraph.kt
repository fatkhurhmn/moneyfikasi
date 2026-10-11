package dev.muffar.moneyfikasi.debt

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.debt.add_edit.navigation.addEditDebtNavigation
import dev.muffar.moneyfikasi.debt.list.navigation.debtsNavigation
import dev.muffar.moneyfikasi.domain.model.DebtType
import java.util.UUID

fun NavGraphBuilder.debtsNavGraph(
    navigateToAddDebt: (DebtType) -> Unit,
    navigateToEditDebt: (DebtType, UUID) -> Unit,
    navigateBack: () -> Unit,
    onNavigateToAddWallet: () -> Unit,
) {
    debtsNavigation(
        navigateToAddDebt = navigateToAddDebt,
        navigateToEditDebt = navigateToEditDebt,
        navigateBack = navigateBack,
    )
    addEditDebtNavigation(
        navigateBack = navigateBack,
        onNavigateToAddWallet = onNavigateToAddWallet,
    )
}
