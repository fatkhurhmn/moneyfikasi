package dev.muffar.moneyfikasi.recurring_transaction.add_edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.tabs.IncomeExpenseTabHeader
import dev.muffar.moneyfikasi.common_ui.component.text_input.BasicAmountInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.CategoryInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.DateInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.WalletInput
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.recurring_transaction.add_edit.AddEditRecurringTransactionState
import dev.muffar.moneyfikasi.resource.R
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
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
    onFrequencyChange: (TimePeriod) -> Unit,
    onStartDateChange: (Long) -> Unit,
    onEndTypeChange: (RecurringEndType) -> Unit,
    onEndDateChange: (Long) -> Unit,
    onOccurrenceCountChange: (String) -> Unit,
) {
    val today = remember {
        LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }

    val selectableDates = remember(today) {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= today
            }
        }
    }

    val selectableEndDate = remember(state.startDate) {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= state.startDate
            }
        }
    }

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

        FormSection(label = stringResource(R.string.transaction)) {
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
        }

        FormSection(label = stringResource(R.string.schedule)) {
            FrequencyInput(
                frequency = state.frequency,
                onFrequencySelect = onFrequencyChange
            )

            DateInput(
                date = state.startDate,
                onDateSelect = onStartDateChange,
                label = stringResource(R.string.start_date),
                selectableDates = selectableDates
            )

            EndRecurringInput(
                endType = state.endType,
                endDate = state.endDate,
                occurrenceCount = state.occurrenceCount,
                selectableDates = selectableEndDate,
                initialDisplayedMonthMillis = state.startDate,
                onEndTypeChange = onEndTypeChange,
                onEndDateChange = onEndDateChange,
                onOccurrenceCountChange = onOccurrenceCountChange
            )
        }

        FormSection(label = stringResource(R.string.preview)) {
            RecurringSummary(state = state)
        }
    }
}

@Composable
private fun FormSection(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        content()
    }
}
