package dev.muffar.moneyfikasi.budget.add_edit.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.muffar.moneyfikasi.budget.add_edit.AddEditBudgetEvent
import dev.muffar.moneyfikasi.budget.add_edit.AddEditBudgetScreen
import dev.muffar.moneyfikasi.budget.add_edit.AddEditBudgetViewModel
import dev.muffar.moneyfikasi.navigation.Screen
import java.util.UUID

fun NavGraphBuilder.addEditBudgetNavigation(
    navigateBack: () -> Unit,
    navigateToAddCategory: () -> Unit,
) {
    composable(
        route = Screen.AddEditBudget.route,
        arguments = listOf(
            navArgument(Screen.AddEditBudget.BUDGET_ID) {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) {
        val viewModel = hiltViewModel<AddEditBudgetViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        AddEditBudgetScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onAmountChange = { viewModel.onEvent(AddEditBudgetEvent.OnAmountChange(it)) },
            onCategorySelect = { viewModel.onEvent(AddEditBudgetEvent.OnCategoryChange(it)) },
            onAddNewCategoryClick = navigateToAddCategory,
            onShowAlert = { viewModel.onEvent(AddEditBudgetEvent.OnShowDeleteAlert(it)) },
            onSubmit = { viewModel.onEvent(AddEditBudgetEvent.OnSaveBudget) },
            onDelete = { viewModel.onEvent(AddEditBudgetEvent.OnDeleteBudget) },
            onBackClick = navigateBack
        )
    }
}

fun NavController.toAddEditBudgetScreen(id: UUID? = null) {
    this.navigate(Screen.AddEditBudget.routeWithArg(id))
}
