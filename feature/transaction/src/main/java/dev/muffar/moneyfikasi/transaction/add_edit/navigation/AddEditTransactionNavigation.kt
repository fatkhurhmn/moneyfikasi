package dev.muffar.moneyfikasi.transaction.add_edit.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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
    composable(Screen.AddEditTransaction.route) {
        val viewModel = hiltViewModel<AddEditTransactionViewModel>()
        val state by viewModel.state.collectAsState()
        val event = viewModel::onEvent
        val eventFlow = viewModel.eventFlow

        val type = it.arguments?.getString(Screen.AddEditTransaction.TYPE)?.let { value ->
            TransactionType.fromString(value)
        }

        LaunchedEffect(Unit) {
            event(AddEditTransactionEvent.InitType(type ?: TransactionType.EXPENSE))
        }

        AddEditTransactionScreen(
            state = state,
            eventFlow = eventFlow,
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

fun NavController.toAddEditTransactionScreen(type: TransactionType, id: UUID? = null) {
    navigate(Screen.AddEditTransaction.routeWithArg(type, id))
}