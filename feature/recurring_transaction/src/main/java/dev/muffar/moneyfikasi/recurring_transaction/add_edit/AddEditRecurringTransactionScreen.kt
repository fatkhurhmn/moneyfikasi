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
                        scheduleRecurringTransaction()
                        if (!event.isEdit) {
                            runRecurringTransactionWorker()
                        }
                    }
                    onBackClick()
                }

                is AddEditRecurringTransactionViewModel.UiEvent.DeleteRecurringTransaction -> {
                    onBackClick()
                }

                is AddEditRecurringTransactionViewModel.UiEvent.ShowMessage -> {
                    snackbarHostState.showMessage(event.message, event.type)
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
                onSave = { onEvent(AddEditRecurringTransactionEvent.OnSaveRecurringTransaction) },
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
            onTypeChange = { onEvent(AddEditRecurringTransactionEvent.OnTypeChanged(it)) },
            onNameChange = { onEvent(AddEditRecurringTransactionEvent.OnNameChanged(it)) },
            onAmountChange = { onEvent(AddEditRecurringTransactionEvent.OnAmountChanged(it)) },
            onCategoryChange = { onEvent(AddEditRecurringTransactionEvent.OnCategoryChanged(it)) },
            onAddNewCategoryClick = onAddNewCategoryClick,
            onWalletChange = { onEvent(AddEditRecurringTransactionEvent.OnWalletChanged(it)) },
            onAddNewWalletClick = onAddNewWalletClick,
            onFrequencyChange = { onEvent(AddEditRecurringTransactionEvent.OnFrequencyChanged(it)) },
            onStartDateChange = { onEvent(AddEditRecurringTransactionEvent.OnStartDateChanged(it)) },
            onEndTypeChange = { onEvent(AddEditRecurringTransactionEvent.OnEndTypeChanged(it)) },
            onEndDateChange = { onEvent(AddEditRecurringTransactionEvent.OnEndDateChanged(it)) },
            onOccurrenceCountChange = { onEvent(AddEditRecurringTransactionEvent.OnOccurrenceCountChanged(it)) },
            onIsSkipFirstChange = { onEvent(AddEditRecurringTransactionEvent.OnIsSkipFirstChanged(it)) }
        )
    }

    if (showDeleteAlert) {
        CommonAlertDialog(
            title = stringResource(R.string.delete_recurring),
            message = stringResource(R.string.delete_recurring_message),
            onConfirm = {
                onEvent(AddEditRecurringTransactionEvent.OnDeleteRecurringTransaction)
                showDeleteAlert = false
            },
            onDismiss = { showDeleteAlert = false },
            positiveText = stringResource(R.string.delete),
            negativeText = stringResource(R.string.cancel)
        )
    }
}
