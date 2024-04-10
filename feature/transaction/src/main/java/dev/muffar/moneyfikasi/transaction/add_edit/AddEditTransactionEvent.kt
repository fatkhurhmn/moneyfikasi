package dev.muffar.moneyfikasi.transaction.add_edit

import dev.muffar.moneyfikasi.domain.model.TransactionType

sealed class AddEditTransactionEvent {
    data class OnInitType(val type: TransactionType) : AddEditTransactionEvent()
}