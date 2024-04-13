package dev.muffar.moneyfikasi.transaction.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
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

    init {
        initState()
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
}