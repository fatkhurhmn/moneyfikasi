package dev.muffar.moneyfikasi.transaction.detail

sealed class TransactionDetailEvent {
    data class OnShowAlert(val showAlert: Boolean) : TransactionDetailEvent()
    data object OnDeleteTransaction : TransactionDetailEvent()
    data object OnInitData : TransactionDetailEvent()
}