package dev.muffar.moneyfikasi.transaction.detail

import android.graphics.Bitmap

sealed class TransactionDetailEvent {
    data class ShowDeleteAlert(val showAlert: Boolean) : TransactionDetailEvent()
    data object DeleteTransaction : TransactionDetailEvent()
    data object InitData : TransactionDetailEvent()
    data class SaveToGallery(val bitmap: Bitmap) : TransactionDetailEvent()
    data class ShowPlusSheet(val show: Boolean) : TransactionDetailEvent()
    data class PlusTabSelected(val index: Int) : TransactionDetailEvent()
    data class PlusAmountChanged(val amount: String) : TransactionDetailEvent()
    data object PlusConfirmed : TransactionDetailEvent()
    data object PlusDismissed : TransactionDetailEvent()
}
