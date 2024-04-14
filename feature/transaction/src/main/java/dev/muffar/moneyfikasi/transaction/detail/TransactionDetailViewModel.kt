package dev.muffar.moneyfikasi.transaction.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.navigation.Screen
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
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionDetailState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        initState()
    }

    fun onEvent(event: TransactionDetailEvent) {
        when (event) {
            is TransactionDetailEvent.OnShowAlert -> onShowAlert(event.showAlert)
            is TransactionDetailEvent.OnDeleteTransaction -> onDeleteTransaction()
        }
    }

    private fun onShowAlert(showAlert: Boolean) {
        _state.update { it.copy(showAlert = showAlert) }
    }

    private fun onDeleteTransaction() {
        state.value.transaction?.let {
            viewModelScope.launch {
                try {
                    val amount = if (it.type == TransactionType.INCOME) -it.amount else it.amount
                    val updatedWallet = it.wallet.copy(balance = it.wallet.balance + amount)
                    transactionUseCases.deleteTransaction(it.id, updatedWallet)
                    _eventFlow.emit(UiEvent.DeleteTransaction)
                } catch (e: Exception) {
                    _eventFlow.emit(UiEvent.ShowMessage("Failed to delete transaction"))
                }
            }
        }
    }

    private fun initState() {
        handle.get<String>(Screen.TransactionDetail.TRANSACTION_ID)?.let {
            val transactionId = UUID.fromString(it)
            viewModelScope.launch {
                transactionUseCases.getTransactionById(transactionId)?.let {
                    _state.update { state -> state.copy(transaction = it) }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String) : UiEvent()
        data object DeleteTransaction : UiEvent()
    }
}