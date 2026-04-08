package dev.muffar.moneyfikasi.transaction.add_edit

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet

sealed class AddEditTransactionEvent {
    data class TypeChanged(val type: TransactionType) : AddEditTransactionEvent()
    data class AmountChanged(val amount: String) : AddEditTransactionEvent()
    data class CategorySelected(val category: Category) : AddEditTransactionEvent()
    data class WalletSelected(val wallet: Wallet) : AddEditTransactionEvent()
    data class DateSelected(val date: Long) : AddEditTransactionEvent()
    data class TimeSelected(val time: Pair<Int, Int>) : AddEditTransactionEvent()
    data class NoteChanged(val note: String) : AddEditTransactionEvent()
    data object SaveTransaction : AddEditTransactionEvent()
}