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
    date: String,
    time: String,
    onAmountChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onCategoryClick: () -> Unit,
    onWalletClick: () -> Unit,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AmountInput(
            value = amount,
            onValueChange = onAmountChange
        )

        NoteInput(
            value = note,
            onValueChange = onNoteChange
        )

        CategoryPicker(
            category = category,
            onCategoryClick = onCategoryClick
        )

        WalletPicker(
            wallet = wallet,
            onWalletClick = onWalletClick
        )

        Row {
            DatePicker(
                modifier = Modifier.weight(0.6f),
                date = date,
                onDateClick = onDateClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            TimePicker(
                modifier = Modifier.weight(0.4f),
                time = time,
                onTimeClick = onTimeClick
            )
        }
    }
}