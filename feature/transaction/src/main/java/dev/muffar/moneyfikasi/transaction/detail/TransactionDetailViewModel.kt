package dev.muffar.moneyfikasi.transaction.detail

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.resource.R
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
        viewModelScope.launch {
            val isTransfer = handle.get<Boolean>(Screen.TransactionDetail.IS_TRANSFER) ?: false
            val transactionId = handle.get<String>(Screen.TransactionDetail.TRANSACTION_ID)
                ?.let { UUID.fromString(it) } ?: return@launch _eventFlow.emit(UiEvent.NavigateBack)

            if (isTransfer) {
                transactionUseCases.getTransferDetail(transactionId).let {
                    if (it == null) {
                        _eventFlow.emit(UiEvent.NavigateBack)
                        return@let
                    }

                    _state.update { state ->
                        state.copy(
                            transactionId = transactionId,
                            transferDetail = it,
                        )
                    }
                }
            } else {
                transactionUseCases.getTransactionById(transactionId).let {
                    if (it == null) {
                        _eventFlow.emit(UiEvent.NavigateBack)
                        return@let
                    }

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
                    _eventFlow.emit(
                        UiEvent.ShowMessage(
                            R.string.failed_to_delete_transaction,
                            SnackbarType.ERROR
                        )
                    )
                }
            }
        }
    }

    private fun onSaveToGallery(bitmap: Bitmap) {
        viewModelScope.launch {
            try {
                transactionUseCases.saveTransactionImage(application, bitmap)
                _eventFlow.emit(UiEvent.ShowMessage(R.string.image_saved, SnackbarType.SUCCESS))
            } catch (e: Exception) {
                e.printStackTrace()
                _eventFlow.emit(UiEvent.ShowMessage(R.string.failed_to_save_image, SnackbarType.ERROR))
            }
        }
    }

    sealed class UiEvent {
        data object DeleteTransaction : UiEvent()
        data class ShowMessage(val messageResId: Int, val type: SnackbarType) : UiEvent()
        data object NavigateBack : UiEvent()
    }
}
