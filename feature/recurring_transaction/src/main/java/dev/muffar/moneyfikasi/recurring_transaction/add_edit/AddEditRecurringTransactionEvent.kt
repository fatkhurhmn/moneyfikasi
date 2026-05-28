package dev.muffar.moneyfikasi.recurring_transaction.add_edit

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet

sealed class AddEditRecurringTransactionEvent {
    data class NameChanged(val name: String) : AddEditRecurringTransactionEvent()
    data class AmountChanged(val amount: String) : AddEditRecurringTransactionEvent()
    data class TypeChanged(val type: TransactionType, val isInit: Boolean = false) : AddEditRecurringTransactionEvent()
    data class CategoryChanged(val category: Category) : AddEditRecurringTransactionEvent()
    data class WalletChanged(val wallet: Wallet) : AddEditRecurringTransactionEvent()
    data class FrequencyChanged(val frequency: TimePeriod) : AddEditRecurringTransactionEvent()
    data class StartDateChanged(val startDate: Long) : AddEditRecurringTransactionEvent()
    data class StartTimeChanged(val startTime: Pair<Int, Int>) : AddEditRecurringTransactionEvent()
    data class EndTypeChanged(val endType: RecurringEndType) : AddEditRecurringTransactionEvent()
    data class EndDateChanged(val endDate: Long) : AddEditRecurringTransactionEvent()
    data class OccurrenceCountChanged(val count: String) : AddEditRecurringTransactionEvent()
    data class IsSkipFirstChanged(val isSkipFirst: Boolean) : AddEditRecurringTransactionEvent()
    data class IsActiveChanged(val isActive: Boolean) : AddEditRecurringTransactionEvent()
    data object SaveRecurringTransaction : AddEditRecurringTransactionEvent()
    data object DeleteRecurringTransaction : AddEditRecurringTransactionEvent()
}
