package dev.muffar.moneyfikasi.budget

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.budget.add_edit.navigation.addEditBudgetNavigation
import dev.muffar.moneyfikasi.budget.list.navigation.budgetsNavigation
import java.util.UUID

fun NavGraphBuilder.budgetsNavGraph(
    navigateToAddBudget: () -> Unit,
    navigateToEditBudget: (UUID) -> Unit,
    navigateToAddCategory: () -> Unit,
    navigateBack: () -> Unit,
) {
    budgetsNavigation(
        navigateToAddBudget = navigateToAddBudget,
        navigateToEditBudget = navigateToEditBudget,
        navigateBack = navigateBack,
    )
    addEditBudgetNavigation(
        navigateBack = navigateBack,
        navigateToAddCategory = navigateToAddCategory,
    )
}
