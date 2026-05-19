package dev.muffar.moneyfikasi.recurring_transaction.add_edit

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet

sealed class AddEditRecurringTransactionEvent {
    data class OnNameChanged(val name: String) : AddEditRecurringTransactionEvent()
    data class OnAmountChanged(val amount: String) : AddEditRecurringTransactionEvent()
    data class OnTypeChanged(val type: TransactionType) : AddEditRecurringTransactionEvent()
    data class OnCategoryChanged(val category: Category) : AddEditRecurringTransactionEvent()
    data class OnWalletChanged(val wallet: Wallet) : AddEditRecurringTransactionEvent()
    data class OnNoteChanged(val note: String) : AddEditRecurringTransactionEvent()
    data class OnFrequencyChanged(val frequency: TimePeriod) : AddEditRecurringTransactionEvent()
    data class OnStartDateChanged(val startDate: Long) : AddEditRecurringTransactionEvent()
    data class OnIsActiveChanged(val isActive: Boolean) : AddEditRecurringTransactionEvent()
    data object OnSaveRecurringTransaction : AddEditRecurringTransactionEvent()
}
