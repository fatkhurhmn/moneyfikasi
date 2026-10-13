package dev.muffar.moneyfikasi.debt.add_edit.navigation

import androidx.compose.runtime.LaunchedEffect
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
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.navigation.Screen
import java.util.UUID

fun NavGraphBuilder.addEditDebtNavigation(
    navigateBack: () -> Unit,
    onNavigateToAddWallet: () -> Unit,
) {
    composable(
        route = Screen.AddEditDebt.route,
        arguments = listOf(
            navArgument(Screen.AddEditDebt.TYPE) { type = NavType.StringType },
            navArgument(Screen.AddEditDebt.DEBT_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
        val viewModel = hiltViewModel<AddEditDebtViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val type = it.arguments?.getString(Screen.AddEditDebt.TYPE)?.let { value ->
            DebtType.fromString(value)
        }

        LaunchedEffect(Unit) {
            viewModel.onEvent(AddEditDebtEvent.TypeChanged(type ?: DebtType.DEBT))
        }

        AddEditDebtScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onTypeChange = { viewModel.onEvent(AddEditDebtEvent.TypeChanged(it)) },
            onNameChange = { viewModel.onEvent(AddEditDebtEvent.NameChanged(it)) },
            onTotalAmountChange = { viewModel.onEvent(AddEditDebtEvent.TotalAmountChanged(it)) },
            onDebtDateChange = { viewModel.onEvent(AddEditDebtEvent.DebtDateChanged(it)) },
            onDueDateChange = { viewModel.onEvent(AddEditDebtEvent.DueDateChanged(it)) },
            onNoteChange = { viewModel.onEvent(AddEditDebtEvent.NoteChanged(it)) },
            onWalletSelect = { viewModel.onEvent(AddEditDebtEvent.WalletSelected(it)) },
            onAddNewWalletClick = onNavigateToAddWallet,
            onShowAlert = { viewModel.onEvent(AddEditDebtEvent.ShowDeleteAlert(it)) },
            onSubmit = { viewModel.onEvent(AddEditDebtEvent.SaveDebt) },
            onDelete = { viewModel.onEvent(AddEditDebtEvent.DeleteDebt) },
            onBackClick = navigateBack
        )
    }
}

fun NavController.toAddEditDebtScreen(type: DebtType, id: UUID? = null) {
    this.navigate(Screen.AddEditDebt.routeWithArg(type, id))
}
