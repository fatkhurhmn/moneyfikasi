package dev.muffar.moneyfikasi.debt.add_edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.text_input.BasicAmountInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.DateInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.WalletInput
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.debt.add_edit.AddEditDebtState
import dev.muffar.moneyfikasi.common_ui.component.tabs.DebtTab
import dev.muffar.moneyfikasi.common_ui.component.tabs.DebtTypeTabHeader
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditDebtForm(
    modifier: Modifier = Modifier,
    state: AddEditDebtState,
    onTypeChange: (DebtType) -> Unit,
    onNameChange: (String) -> Unit,
    onTotalAmountChange: (String) -> Unit,
    onDebtDateChange: (Long) -> Unit,
    onDueDateChange: (Long?) -> Unit,
    onNoteChange: (String) -> Unit,
    onWalletSelect: (Wallet) -> Unit,
    onAddNewWalletClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val financeColors = MoneyfikasiTheme.financeColors
        DebtTypeTabHeader(
            tabs = listOf(
                DebtTab(R.string.label_hutang, financeColors.income),
                DebtTab(R.string.label_piutang, financeColors.expense)
            ),
            selectedTab = if (state.type == DebtType.DEBT) 0 else 1,
            onTabSelected = {
                onTypeChange(if (it == 0) DebtType.DEBT else DebtType.RECEIVABLE)
            }
        )

        CommonTextInput(
            value = state.name,
            onValueChange = onNameChange,
            label = stringResource(R.string.label_name),
            error = state.nameError
        )

        BasicAmountInput(
            amount = state.totalAmount,
            error = state.totalAmountError,
            onAmountChange = onTotalAmountChange,
        )

        WalletInput(
            wallet = state.wallet,
            error = state.walletError,
            walletOptions = state.walletOptions,
            onWalletSelect = onWalletSelect,
            onAddNewWalletClick = onAddNewWalletClick
        )

        DateInput(
            date = state.createdAt,
            label = stringResource(R.string.label_debt_date),
            onDateSelect = onDebtDateChange
        )

        DateInput(
            date = state.dueDate,
            label = stringResource(R.string.label_due_date),
            error = state.dueDateError,
            selectableDates = remember(state.createdAt) {
                val minDate = Instant.ofEpochMilli(state.createdAt)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        val selected = Instant.ofEpochMilli(utcTimeMillis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        return !selected.isBefore(minDate)
                    }

                    override fun isSelectableYear(year: Int): Boolean {
                        return year >= minDate.year
                    }
                }
            },
            onDateSelect = onDueDateChange
        )

        CommonTextInput(
            value = state.note,
            onValueChange = onNoteChange,
            label = stringResource(R.string.label_note_optional),
        )
    }
}
