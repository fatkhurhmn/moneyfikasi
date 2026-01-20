package dev.muffar.moneyfikasi.transaction.detail

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.Message
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    private val handle: SavedStateHandle,
    private val application: Application
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(TransactionDetailState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: TransactionDetailEvent) {
        when (event) {
            is TransactionDetailEvent.ShowDeleteAlert -> onShowDeleteAlert(event.showAlert)
            is TransactionDetailEvent.DeleteTransaction -> onDeleteTransaction()
            is TransactionDetailEvent.InitData -> onInitData()
            is TransactionDetailEvent.SaveToGallery -> onSaveToGallery(event.bitmap)
        }
    }

    private fun onInitData() {
        val isTransfer = handle.get<Boolean>(Screen.TransactionDetail.IS_TRANSFER) ?: false
        val transactionId = handle.get<String>(Screen.TransactionDetail.TRANSACTION_ID)
            ?.let { UUID.fromString(it) } ?: return

        viewModelScope.launch {
            if (isTransfer) {
                transactionUseCases.getTransferDetail(transactionId)?.let {
                    _state.update { state ->
                        state.copy(
                            transactionId = transactionId,
                            transferDetail = it,
                        )
                    }
                }
            } else {
                transactionUseCases.getTransactionById(transactionId)?.let {
                    _state.update { state ->
                        state.copy(
                            transactionId = transactionId,
                            transaction = it,
                        )
                    }
                }
            }
        }
    }

    private fun onShowDeleteAlert(showAlert: Boolean) {
        _state.update { it.copy(showAlert = showAlert) }
    }

    private fun onDeleteTransaction() {
        state.value.transactionId?.let {
            viewModelScope.launch {
                try {
                    transactionUseCases.deleteTransaction(it)
                    _eventFlow.emit(UiEvent.DeleteTransaction)
                } catch (e: Exception) {
                    e.printStackTrace()
                    onShowPopUpMessage("Failed to delete transaction", true)
                }
            }
        }
    }

    private fun onShowPopUpMessage(message: String, error: Boolean) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    message = Message(message, error),
                    messageVisibility = true
                )
            }
            delay(2000)
            _state.update { it.copy(messageVisibility = false) }
        }
    }

    private fun onSaveToGallery(bitmap: Bitmap) {
        try {
            transactionUseCases.saveTransactionImage(application, bitmap)
            onShowPopUpMessage("Image saved", false)
        } catch (e: Exception) {
            e.printStackTrace()
            onShowPopUpMessage("Failed to save image", true)
        }
    }

    sealed class UiEvent {
        data object DeleteTransaction : UiEvent()
    }
}