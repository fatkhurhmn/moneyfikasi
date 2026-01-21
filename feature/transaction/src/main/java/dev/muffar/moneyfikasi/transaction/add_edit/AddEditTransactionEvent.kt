package dev.muffar.moneyfikasi.transaction.add_edit

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet

sealed class AddEditTransactionEvent {
    data class OnInitType(val type: TransactionType) : AddEditTransactionEvent()
    data class OnAmountChange(val amount: String) : AddEditTransactionEvent()
    data class OnCategorySelect(val category: Category) : AddEditTransactionEvent()
    data class OnWalletSelect(val wallet: Wallet) : AddEditTransactionEvent()
    data class OnDateSelect(val date: Long) : AddEditTransactionEvent()
    data class OnTimeSelect(val time: Pair<Int, Int>) : AddEditTransactionEvent()
    data class OnNoteChange(val note: String) : AddEditTransactionEvent()
    data object OnCreateClicked : AddEditTransactionEvent()
}