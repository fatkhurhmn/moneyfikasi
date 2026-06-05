package dev.muffar.moneyfikasi.recurring_transaction.add_edit

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.rememberScrollState
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
import dev.muffar.moneyfikasi.common_ui.component.ModifierExt.formModifier
import dev.muffar.moneyfikasi.common_ui.component.button.bottom_bar.BottomBarAddEditButton
import dev.muffar.moneyfikasi.common_ui.component.dialog.CommonAlertDialog
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.data.utils.RecurringTransactionScheduler
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.recurring_transaction.add_edit.component.AddEditRecurringTransactionForm
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditRecurringTransactionScreen(
    state: AddEditRecurringTransactionState,
    eventFlow: SharedFlow<AddEditRecurringTransactionViewModel.UiEvent>,
    onTypeChange: (TransactionType) -> Unit,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onAddNewCategoryClick: () -> Unit,
    onWalletChange: (Wallet) -> Unit,
    onAddNewWalletClick: () -> Unit,
    onFrequencyChange: (TimePeriod) -> Unit,
    onStartDateChange: (Long) -> Unit,
    onStartTimeChange: (Pair<Int, Int>) -> Unit,
    onEndTypeChange: (RecurringEndType) -> Unit,
    onEndDateChange: (Long) -> Unit,
    onOccurrenceCountChange: (String) -> Unit,
    onIsSkipFirstChange: (Boolean) -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
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
                    stringResource(R.string.action_add_recurring)
                } else {
                    stringResource(R.string.action_edit_recurring)
                },
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BottomBarAddEditButton(
                isEdit = state.id != null,
                onSave = onSaveClick,
                onDelete = { showDeleteAlert = true }
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) {innerPadding->
        AddEditRecurringTransactionForm(
            modifier = Modifier.formModifier(innerPadding, scrollState),
            state = state,
            onTypeChange = onTypeChange,
            onNameChange = onNameChange,
            onAmountChange = onAmountChange,
            onCategoryChange = onCategoryChange,
            onAddNewCategoryClick = onAddNewCategoryClick,
            onWalletChange = onWalletChange,
            onAddNewWalletClick = onAddNewWalletClick,
            onFrequencyChange = onFrequencyChange,
            onStartDateChange = onStartDateChange,
            onStartTimeChange = onStartTimeChange,
            onEndTypeChange = onEndTypeChange,
            onEndDateChange = onEndDateChange,
            onOccurrenceCountChange = onOccurrenceCountChange,
            onIsSkipFirstChange = onIsSkipFirstChange
        )
    }

    if (showDeleteAlert) {
        CommonAlertDialog(
            title = stringResource(R.string.title_delete_recurring),
            message = stringResource(R.string.msg_delete_recurring_confirmation),
            onConfirm = {
                onDeleteClick()
                showDeleteAlert = false
            },
            onDismiss = { showDeleteAlert = false },
            positiveText = stringResource(R.string.action_delete),
            negativeText = stringResource(R.string.action_cancel)
        )
    }
}
