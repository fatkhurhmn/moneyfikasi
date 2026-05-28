package dev.muffar.moneyfikasi.recurring_transaction.add_edit

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.bottom_bar.BottomBarAddEditButton
import dev.muffar.moneyfikasi.common_ui.component.dialog.CommonAlertDialog
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.data.utils.RecurringTransactionScheduler
import dev.muffar.moneyfikasi.recurring_transaction.add_edit.component.AddEditRecurringTransactionForm
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditRecurringTransactionScreen(
    modifier: Modifier = Modifier,
    state: AddEditRecurringTransactionState,
    eventFlow: SharedFlow<AddEditRecurringTransactionViewModel.UiEvent>,
    onEvent: (AddEditRecurringTransactionEvent) -> Unit,
    onAddNewCategoryClick: () -> Unit,
    onAddNewWalletClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    var showDeleteAlert by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is AddEditRecurringTransactionViewModel.UiEvent.SaveRecurringTransaction -> {
                    RecurringTransactionScheduler(context).apply {
                        updateRecurringTransactionSchedule(event.hasActive)
                        if (event.hasActive) runRecurringTransactionWorker()
                    }
                    onBackClick()
                }

                is AddEditRecurringTransactionViewModel.UiEvent.DeleteRecurringTransaction -> {
                    RecurringTransactionScheduler(context).updateRecurringTransactionSchedule(event.hasActive)
                    onBackClick()
                }

                is AddEditRecurringTransactionViewModel.UiEvent.ShowMessage -> {
                    snackbarHostState.showMessage(
                        context.applicationContext.getString(event.messageResId),
                        event.type
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarMessage(state = snackbarHostState) },
        topBar = {
            CommonTopAppBar(
                title = if (state.id == null) {
                    stringResource(R.string.add_recurring)
                } else {
                    stringResource(R.string.edit_recurring)
                },
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BottomBarAddEditButton(
                isEdit = state.id != null,
                onSave = { onEvent(AddEditRecurringTransactionEvent.SaveRecurringTransaction) },
                onDelete = { showDeleteAlert = true }
            )
        }
    ) { paddingValues ->
        AddEditRecurringTransactionForm(
            modifier = modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .imePadding()
                .verticalScroll(scrollState)
                .padding(16.dp),
            state = state,
            onTypeChange = { onEvent(AddEditRecurringTransactionEvent.TypeChanged(it, false)) },
            onNameChange = { onEvent(AddEditRecurringTransactionEvent.NameChanged(it)) },
            onAmountChange = { onEvent(AddEditRecurringTransactionEvent.AmountChanged(it)) },
            onCategoryChange = { onEvent(AddEditRecurringTransactionEvent.CategoryChanged(it)) },
            onAddNewCategoryClick = onAddNewCategoryClick,
            onWalletChange = { onEvent(AddEditRecurringTransactionEvent.WalletChanged(it)) },
            onAddNewWalletClick = onAddNewWalletClick,
            onFrequencyChange = { onEvent(AddEditRecurringTransactionEvent.FrequencyChanged(it)) },
            onStartDateChange = { onEvent(AddEditRecurringTransactionEvent.StartDateChanged(it)) },
            onStartTimeChange = { onEvent(AddEditRecurringTransactionEvent.StartTimeChanged(it)) },
            onEndTypeChange = { onEvent(AddEditRecurringTransactionEvent.EndTypeChanged(it)) },
            onEndDateChange = { onEvent(AddEditRecurringTransactionEvent.EndDateChanged(it)) },
            onOccurrenceCountChange = {
                onEvent(
                    AddEditRecurringTransactionEvent.OccurrenceCountChanged(
                        it
                    )
                )
            },
            onIsSkipFirstChange = { onEvent(AddEditRecurringTransactionEvent.IsSkipFirstChanged(it)) }
        )
    }

    if (showDeleteAlert) {
        CommonAlertDialog(
            title = stringResource(R.string.delete_recurring),
            message = stringResource(R.string.delete_recurring_message),
            onConfirm = {
                onEvent(AddEditRecurringTransactionEvent.DeleteRecurringTransaction)
                showDeleteAlert = false
            },
            onDismiss = { showDeleteAlert = false },
            positiveText = stringResource(R.string.delete),
            negativeText = stringResource(R.string.cancel)
        )
    }
}
