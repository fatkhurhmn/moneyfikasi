package dev.muffar.moneyfikasi.budget.list.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.budget.list.BudgetsScreen
import dev.muffar.moneyfikasi.budget.list.BudgetsViewModel
import dev.muffar.moneyfikasi.navigation.Screen
import java.util.UUID

fun NavController.toBudgetsScreen(navOptions: NavOptions? = null) {
    this.navigate(Screen.Budgets.route, navOptions)
}

fun NavGraphBuilder.budgetsNavigation(
    navigateToAddBudget: () -> Unit,
    navigateToEditBudget: (UUID) -> Unit,
    navigateBack: () -> Unit,
) {
    composable(route = Screen.Budgets.route) {
        val viewModel: BudgetsViewModel = hiltViewModel()
        val state by viewModel.state

        BudgetsScreen(
            state = state,
            onBudgetClick = navigateToEditBudget,
            onAddBudgetClick = navigateToAddBudget,
            onBackClick = navigateBack
        )
    }
}
