package dev.muffar.moneyfikasi.debt.add_edit.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.muffar.moneyfikasi.debt.add_edit.AddEditDebtEvent
import dev.muffar.moneyfikasi.debt.add_edit.AddEditDebtScreen
import dev.muffar.moneyfikasi.debt.add_edit.AddEditDebtViewModel
import dev.muffar.moneyfikasi.navigation.Screen
import java.util.UUID

fun NavGraphBuilder.addEditDebtNavigation(
    navigateBack: () -> Unit,
) {
    composable(
        route = Screen.AddEditDebt.route,
        arguments = listOf(
            navArgument(Screen.AddEditDebt.DEBT_ID) {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) {
        val viewModel = hiltViewModel<AddEditDebtViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        AddEditDebtScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onNameChange = { viewModel.onEvent(AddEditDebtEvent.NameChanged(it)) },
            onTotalAmountChange = { viewModel.onEvent(AddEditDebtEvent.TotalAmountChanged(it)) },
            onPaidAmountChange = { viewModel.onEvent(AddEditDebtEvent.PaidAmountChanged(it)) },
            onDueDateChange = { viewModel.onEvent(AddEditDebtEvent.DueDateChanged(it)) },
            onNoteChange = { viewModel.onEvent(AddEditDebtEvent.NoteChanged(it)) },
            onShowAlert = { viewModel.onEvent(AddEditDebtEvent.ShowDeleteAlert(it)) },
            onSubmit = { viewModel.onEvent(AddEditDebtEvent.SaveDebt) },
            onDelete = { viewModel.onEvent(AddEditDebtEvent.DeleteDebt) },
            onBackClick = navigateBack
        )
    }
}

fun NavController.toAddEditDebtScreen(id: UUID? = null) {
    this.navigate(Screen.AddEditDebt.routeWithArg(id))
}
