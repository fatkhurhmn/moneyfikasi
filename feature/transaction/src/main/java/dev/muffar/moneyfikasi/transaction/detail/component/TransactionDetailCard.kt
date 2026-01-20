package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.Transaction

@Composable
fun TransactionDetailCard(
    transaction: Transaction
) {
    TransactionDetailHeader(transaction.type)
    Spacer(modifier = Modifier.height(32.dp))
    TransactionDetailAmount(
        amount = transaction.amount,
        type = transaction.type
    )
    TransactionDetailDivider()
    TransactionDetailBody(
        date = transaction.date,
        wallet = transaction.wallet,
        category = transaction.category,
    )
    val note = transaction.note
    if (!note.isNullOrBlank()) {
        TransactionDetailDivider()
        TransactionDetailNote(note)
    }
}