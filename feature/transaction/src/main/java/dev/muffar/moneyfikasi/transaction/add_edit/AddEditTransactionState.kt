package dev.muffar.moneyfikasi.transaction.add_edit

import dev.muffar.moneyfikasi.domain.model.TransactionType

data class AddEditTransactionState(
    val type: TransactionType = TransactionType.EXPENSE,
)