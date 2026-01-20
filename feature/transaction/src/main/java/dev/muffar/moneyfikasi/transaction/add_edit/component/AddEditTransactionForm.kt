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
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Wallet

@Composable
fun AddEditTransactionForm(
    amount: String,
    note: String,
    category: Category,
    wallet: Wallet,
    date: Long,
    time: Pair<Int, Int>,
    walletOption: List<Wallet>,
    onAmountChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onCategoryClick: () -> Unit,
    onWalletSelect: (Wallet) -> Unit,
    onAddNewWalletClick: () -> Unit,
    onDateSelect: (Long) -> Unit,
    onTimeSelect: (Pair<Int, Int>) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AmountInput(
            amount = amount,
            onAmountChange = onAmountChange
        )

        NoteInput(
            note = note,
            onNoteChange = onNoteChange
        )

        CategoryInput(
            category = category,
            onCategoryClick = onCategoryClick
        )

        WalletInput(
            wallet = wallet,
            walletOption = walletOption,
            onWalletSelect = onWalletSelect,
            onAddNewWalletClick = onAddNewWalletClick
        )

        Row {
            DateInput(
                modifier = Modifier.weight(0.6f),
                date = date,
                onDateSelect = onDateSelect
            )
            Spacer(modifier = Modifier.width(16.dp))
            TimeInput(
                modifier = Modifier.weight(0.4f),
                time = time,
                onTimeSelect = onTimeSelect
            )
        }
    }
}