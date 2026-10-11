package dev.muffar.moneyfikasi.debt.add_edit

sealed class AddEditDebtEvent {
    data class NameChanged(val name: String) : AddEditDebtEvent()
    data class TotalAmountChanged(val amount: String) : AddEditDebtEvent()
    data class PaidAmountChanged(val amount: String) : AddEditDebtEvent()
    data class DueDateChanged(val dueDate: Long?) : AddEditDebtEvent()
    data class NoteChanged(val note: String) : AddEditDebtEvent()
    data object SaveDebt : AddEditDebtEvent()
    data object DeleteDebt : AddEditDebtEvent()
    data class ShowDeleteAlert(val show: Boolean) : AddEditDebtEvent()
}
