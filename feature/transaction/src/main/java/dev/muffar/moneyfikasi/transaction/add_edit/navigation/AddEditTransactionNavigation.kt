package dev.muffar.moneyfikasi.transaction.add_edit.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.transaction.add_edit.AddEditTransactionEvent
import dev.muffar.moneyfikasi.transaction.add_edit.AddEditTransactionScreen
import dev.muffar.moneyfikasi.transaction.add_edit.AddEditTransactionViewModel
import java.util.UUID

fun NavGraphBuilder.addEditTransactionNavigation(
    onNavigateBack: () -> Unit,
    onNavigateToAddWallet: () -> Unit,
    onNavigateToAddCategory: (CategoryType) -> Unit
) {
    composable(
        route = Screen.AddEditTransaction.route,
        arguments = listOf(
            navArgument(Screen.AddEditTransaction.TYPE) { type = NavType.StringType },
            navArgument(Screen.AddEditTransaction.TRANSACTION_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(Screen.AddEditTransaction.PRESET_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
        val viewModel = hiltViewModel<AddEditTransactionViewModel>()
        val state by viewModel.state.collectAsState()
        val event = viewModel::onEvent
        val eventFlow = viewModel.eventFlow

        val type = it.arguments?.getString(Screen.AddEditTransaction.TYPE)?.let { value ->
            TransactionType.fromString(value)
        }

        LaunchedEffect(Unit) {
            event(AddEditTransactionEvent.TypeChanged(type ?: TransactionType.EXPENSE, true))
        }

        AddEditTransactionScreen(
            state = state,
            eventFlow = eventFlow,
            onTypeChange = { type ->
                event(AddEditTransactionEvent.TypeChanged(type, false))
            },
            onAmountChange = { amount ->
                event(AddEditTransactionEvent.AmountChanged(amount))
            },
            onNoteChange = { note ->
                event(AddEditTransactionEvent.NoteChanged(note))
            },
            onCategorySelect = { category ->
                event(AddEditTransactionEvent.CategorySelected(category))
            },
            onWalletSelect = { wallet ->
                event(AddEditTransactionEvent.WalletSelected(wallet))
            },
            onDateSelect = { date ->
                event(AddEditTransactionEvent.DateSelected(date))
            },
            onTimeSelect = { time ->
                event(AddEditTransactionEvent.TimeSelected(time))
            },
            onAddNewWalletClick = onNavigateToAddWallet,
            onAddNewCategoryClick = { onNavigateToAddCategory(state.categoryType) },
            onSaveClick = { event(AddEditTransactionEvent.SaveTransaction) },
            onBackClick = onNavigateBack
        )
    }
}

fun NavController.toAddEditTransactionScreen(
    type: TransactionType,
    transactionId: UUID? = null,
    presetId: UUID? = null
) {
    navigate(Screen.AddEditTransaction.routeWithArg(type, transactionId, presetId))
}
