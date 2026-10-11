package dev.muffar.moneyfikasi.debt.add_edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.text_input.BasicAmountInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.DateInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.WalletInput
import dev.muffar.moneyfikasi.debt.add_edit.AddEditDebtState
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AddEditDebtForm(
    modifier: Modifier = Modifier,
    state: AddEditDebtState,
    onNameChange: (String) -> Unit,
    onTotalAmountChange: (String) -> Unit,
    onPaidAmountChange: (String) -> Unit,
    onDueDateChange: (Long?) -> Unit,
    onNoteChange: (String) -> Unit,
    onWalletSelect: (Wallet) -> Unit,
    onAddNewWalletClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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

        BasicAmountInput(
            amount = state.paidAmount,
            error = state.paidAmountError,
            label = stringResource(R.string.label_paid_amount),
            onAmountChange = onPaidAmountChange,
        )

        WalletInput(
            wallet = state.wallet,
            error = state.walletError,
            walletOptions = state.walletOptions,
            onWalletSelect = onWalletSelect,
            onAddNewWalletClick = onAddNewWalletClick
        )

        DateInput(
            date = state.dueDate ?: System.currentTimeMillis(),
            label = stringResource(R.string.label_due_date),
            onDateSelect = onDueDateChange
        )

        if (state.dueDate != null) {
            TextButton(onClick = { onDueDateChange(null) }) {
                Text(text = stringResource(R.string.action_clear))
            }
        }

        CommonTextInput(
            value = state.note,
            onValueChange = onNoteChange,
            label = stringResource(R.string.label_note_optional),
        )
    }
}
