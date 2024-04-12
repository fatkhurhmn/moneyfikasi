package dev.muffar.moneyfikasi.transaction.add_edit

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.transaction.add_edit.component.AddEditTransactionSheetType

sealed class AddEditTransactionEvent {
    data class OnInitType(val type: TransactionType) : AddEditTransactionEvent()
    data class OnAmountChange(val amount: String) : AddEditTransactionEvent()
    data class OnCategorySelect(val category: Category) : AddEditTransactionEvent()
    data class OnWalletSelect(val wallet: Wallet) : AddEditTransactionEvent()
    data class OnDateSelect(val date: Long) : AddEditTransactionEvent()
    data class OnTimeSelect(val hour: Int, val minute: Int) : AddEditTransactionEvent()
    data class OnDescriptionChange(val description: String) : AddEditTransactionEvent()
    data object OnSaveClick : AddEditTransactionEvent()
    data object OnDeleteClick : AddEditTransactionEvent()
    data class OnBottomSheetChange(val type: AddEditTransactionSheetType?) : AddEditTransactionEvent()
}