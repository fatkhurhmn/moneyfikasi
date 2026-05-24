package dev.muffar.moneyfikasi.recurring_transaction.add_edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.tabs.IncomeExpenseTabHeader
import dev.muffar.moneyfikasi.common_ui.component.text_input.BasicAmountInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.CategoryInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.DateInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.DescriptionInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.WalletInput
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.recurring_transaction.add_edit.AddEditRecurringTransactionState
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AddEditRecurringTransactionForm(
    modifier: Modifier = Modifier,
    state: AddEditRecurringTransactionState,
    onTypeChange: (TransactionType) -> Unit,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onAddNewCategoryClick: () -> Unit,
    onWalletChange: (Wallet) -> Unit,
    onAddNewWalletClick: () -> Unit,
    onNoteChange: (String) -> Unit,
    onFrequencyChange: (TimePeriod) -> Unit,
    onStartDateChange: (Long) -> Unit,
    onEndTypeChange: (RecurringEndType) -> Unit,
    onEndDateChange: (Long) -> Unit,
    onOccurrenceCountChange: (String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IncomeExpenseTabHeader(
            selectedTab = if (state.type == TransactionType.INCOME) 0 else 1,
            onTabSelected = {
                val type = if (it == 0) TransactionType.INCOME else TransactionType.EXPENSE
                onTypeChange(type)
            }
        )

        CommonTextInput(
            value = state.name,
            onValueChange = onNameChange,
            label = stringResource(R.string.name),
            error = state.nameError
        )

        BasicAmountInput(
            amount = state.amount,
            onAmountChange = onAmountChange,
            error = state.amountError
        )

        CategoryInput(
            category = state.category ?: Category(),
            categoryOptions = state.categories,
            onCategorySelect = onCategoryChange,
            onAddNewCategoryClick = onAddNewCategoryClick,
            error = state.categoryError
        )

        WalletInput(
            wallet = state.wallet ?: Wallet(),
            walletOptions = state.wallets,
            onWalletSelect = onWalletChange,
            onAddNewWalletClick = onAddNewWalletClick,
            error = state.walletError
        )

        FrequencyInput(
            frequency = state.frequency,
            onFrequencySelect = onFrequencyChange
        )

        DateInput(
            date = state.startDate,
            onDateSelect = onStartDateChange,
            label = stringResource(R.string.start_date)
        )

        EndRecurringInput(
            endType = state.endType,
            endDate = state.endDate,
            occurrenceCount = state.occurrenceCount,
            endDateError = state.endDateError,
            onEndTypeChange = onEndTypeChange,
            onEndDateChange = onEndDateChange,
            onOccurrenceCountChange = onOccurrenceCountChange
        )

        DescriptionInput(
            description = state.note,
            onDescriptionChange = onNoteChange
        )

        RecurringSummary(state = state)
    }
}
