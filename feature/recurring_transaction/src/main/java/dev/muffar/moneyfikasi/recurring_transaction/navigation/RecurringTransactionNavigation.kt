package dev.muffar.moneyfikasi.recurring_transaction.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.recurring_transaction.add_edit.AddEditRecurringTransactionEvent
import dev.muffar.moneyfikasi.recurring_transaction.add_edit.AddEditRecurringTransactionScreen
import dev.muffar.moneyfikasi.recurring_transaction.add_edit.AddEditRecurringTransactionViewModel
import dev.muffar.moneyfikasi.recurring_transaction.list.RecurringTransactionsEvent
import dev.muffar.moneyfikasi.recurring_transaction.list.RecurringTransactionsScreen
import dev.muffar.moneyfikasi.recurring_transaction.list.RecurringTransactionsViewModel
import java.util.UUID

fun NavController.toRecurringTransactionsScreen() {
    navigate(Screen.RecurringTransactions.route)
}

fun NavController.toAddEditRecurringTransactionScreen(type: TransactionType, id: UUID? = null) {
    navigate(Screen.AddEditRecurringTransaction.routeWithArg(type, id))
}

fun NavGraphBuilder.recurringTransactionNavGraph(
    navigateToAddRecurringTransaction: (TransactionType) -> Unit,
    navigateToEditRecurringTransaction: (TransactionType, UUID) -> Unit,
    navigateToAddCategory: (dev.muffar.moneyfikasi.domain.model.CategoryType) -> Unit,
    navigateToAddWallet: () -> Unit,
    navigateBack: () -> Unit
) {
    composable(route = Screen.RecurringTransactions.route) {
        val viewModel = hiltViewModel<RecurringTransactionsViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        RecurringTransactionsScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onAddRecurringTransactionClick = navigateToAddRecurringTransaction,
            onRecurringTransactionClick = { id ->
                val recurringTransaction = state.recurringTransactions.find { it.id == id }
                recurringTransaction?.let {
                    navigateToEditRecurringTransaction(it.type, it.id)
                }
            },
            onToggleActive = {
                viewModel.onEvent(
                    RecurringTransactionsEvent.ToggleRecurringTransaction(
                        it
                    )
                )
            },
            onBackClick = navigateBack
        )
    }

    composable(
        route = Screen.AddEditRecurringTransaction.route,
        arguments = listOf(
            navArgument(Screen.AddEditRecurringTransaction.TYPE) { type = NavType.StringType },
            navArgument(Screen.AddEditRecurringTransaction.RECURRING_TRANSACTION_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern =
                    "moneyfikasi://add_edit_recurring_transaction/{type}?recurring_transaction_id={recurring_transaction_id}"
            }
        )
    ) {
        val viewModel = hiltViewModel<AddEditRecurringTransactionViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val type = it.arguments?.getString(Screen.AddEditRecurringTransaction.TYPE)?.let { value ->
            TransactionType.fromString(value)
        }

        LaunchedEffect(Unit) {
            event(AddEditRecurringTransactionEvent.TypeChanged(type ?: TransactionType.EXPENSE, true))
        }

        AddEditRecurringTransactionScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onTypeChange = { type ->
                event(AddEditRecurringTransactionEvent.TypeChanged(type, false))
            },
            onNameChange = { name ->
                event(AddEditRecurringTransactionEvent.NameChanged(name))
            },
            onAmountChange = { amount ->
                event(AddEditRecurringTransactionEvent.AmountChanged(amount))
            },
            onCategoryChange = { category ->
                event(AddEditRecurringTransactionEvent.CategoryChanged(category))
            },
            onAddNewCategoryClick = { navigateToAddCategory(state.recurringTransaction.type.toCategoryType()) },
            onWalletChange = { wallet ->
                event(AddEditRecurringTransactionEvent.WalletChanged(wallet))
            },
            onAddNewWalletClick = navigateToAddWallet,
            onFrequencyChange = { frequency ->
                event(AddEditRecurringTransactionEvent.FrequencyChanged(frequency))
            },
            onStartDateChange = { startDate ->
                event(AddEditRecurringTransactionEvent.StartDateChanged(startDate))
            },
            onStartTimeChange = { startTime ->
                event(AddEditRecurringTransactionEvent.StartTimeChanged(startTime))
            },
            onEndTypeChange = { endType ->
                event(AddEditRecurringTransactionEvent.EndTypeChanged(endType))
            },
            onEndDateChange = { endDate ->
                event(AddEditRecurringTransactionEvent.EndDateChanged(endDate))
            },
            onOccurrenceCountChange = { count ->
                event(AddEditRecurringTransactionEvent.OccurrenceCountChanged(count))
            },
            onIsSkipFirstChange = { isSkipFirst ->
                event(AddEditRecurringTransactionEvent.IsSkipFirstChanged(isSkipFirst))
            },
            onSaveClick = { event(AddEditRecurringTransactionEvent.SaveRecurringTransaction) },
            onDeleteClick = { event(AddEditRecurringTransactionEvent.DeleteRecurringTransaction) },
            onBackClick = navigateBack
        )
    }
}
