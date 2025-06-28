package dev.muffar.moneyfikasi.transaction.detail

import android.graphics.Bitmap

sealed class TransactionDetailEvent {
    data class OnShowAlert(val showAlert: Boolean) : TransactionDetailEvent()
    data object OnDeleteTransaction : TransactionDetailEvent()
    data object OnInitData : TransactionDetailEvent()
    data class OnSaveToGallery(val bitmap: Bitmap) : TransactionDetailEvent()
}