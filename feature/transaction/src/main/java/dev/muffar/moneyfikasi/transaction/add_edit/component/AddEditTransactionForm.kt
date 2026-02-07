package dev.muffar.moneyfikasi.transaction.add_edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.text_input.AmountInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.DateInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.TimeInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.WalletInput
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.transaction.add_edit.AddEditTransactionState

@Composable
fun AddEditTransactionForm(
    modifier: Modifier = Modifier,
    state: AddEditTransactionState,
    onAmountChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onCategorySelect: (Category) -> Unit,
    onAddNewCategoryClick: () -> Unit,
    onWalletSelect: (Wallet) -> Unit,
    onAddNewWalletClick: () -> Unit,
    onDateSelect: (Long) -> Unit,
    onTimeSelect: (Pair<Int, Int>) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AmountInput(
            amount = state.amount,
            error = state.amountError,
            onAmountChange = onAmountChange
        )

        CategoryInput(
            category = state.category,
            error = state.categoryError,
            categoryOptions = state.categoryOptions,
            onCategorySelect = onCategorySelect,
            onAddNewCategoryClick = onAddNewCategoryClick
        )

        WalletInput(
            wallet = state.wallet,
            error = state.walletError,
            walletOptions = state.walletOptions,
            onWalletSelect = onWalletSelect,
            onAddNewWalletClick = onAddNewWalletClick
        )

        Row {
            DateInput(
                modifier = Modifier.weight(0.6f),
                date = state.date,
                onDateSelect = onDateSelect
            )
            Spacer(modifier = Modifier.width(16.dp))
            TimeInput(
                modifier = Modifier.weight(0.4f),
                time = state.hour to state.minute,
                onTimeSelect = onTimeSelect
            )
        }

        NoteInput(
            note = state.note,
            onNoteChange = onNoteChange
        )
    }
}