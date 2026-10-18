package dev.muffar.moneyfikasi.debt.add_edit

import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.Wallet

sealed class AddEditDebtEvent {
    data class TypeChanged(val type: DebtType) : AddEditDebtEvent()
    data class NameChanged(val name: String) : AddEditDebtEvent()
    data class TotalAmountChanged(val amount: String) : AddEditDebtEvent()
    data class PaidAmountChanged(val amount: String) : AddEditDebtEvent()
    data class DebtDateChanged(val date: Long) : AddEditDebtEvent()
    data class DueDateChanged(val dueDate: Long?) : AddEditDebtEvent()
    data class NoteChanged(val note: String) : AddEditDebtEvent()
    data class WalletSelected(val wallet: Wallet) : AddEditDebtEvent()
    data object SaveDebt : AddEditDebtEvent()
}
