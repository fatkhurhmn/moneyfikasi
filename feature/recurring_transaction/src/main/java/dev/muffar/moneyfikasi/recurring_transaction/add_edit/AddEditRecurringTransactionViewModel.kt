package dev.muffar.moneyfikasi.recurring_transaction.add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.recurring_transaction.RecurringTransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditRecurringTransactionViewModel @Inject constructor(
    private val recurringTransactionUseCases: RecurringTransactionUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val walletUseCases: WalletUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditRecurringTransactionState())
    val state = _state.asStateFlow()

    private val _eventFlow = Channel<UiEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        val recurringTransactionId = savedStateHandle.get<String>(Screen.AddEditRecurringTransaction.RECURRING_TRANSACTION_ID)
        if (recurringTransactionId != null && recurringTransactionId.isNotEmpty()) {
            loadRecurringTransaction(UUID.fromString(recurringTransactionId))
        }
        observeData()
    }

    fun onEvent(event: AddEditRecurringTransactionEvent) {
        when (event) {
            is AddEditRecurringTransactionEvent.OnNameChanged -> _state.update { it.copy(name = event.name) }
            is AddEditRecurringTransactionEvent.OnAmountChanged -> _state.update { it.copy(amount = event.amount) }
            is AddEditRecurringTransactionEvent.OnTypeChanged -> _state.update { it.copy(type = event.type) }
            is AddEditRecurringTransactionEvent.OnCategoryChanged -> _state.update { it.copy(category = event.category) }
            is AddEditRecurringTransactionEvent.OnWalletChanged -> _state.update { it.copy(wallet = event.wallet) }
            is AddEditRecurringTransactionEvent.OnNoteChanged -> _state.update { it.copy(note = event.note) }
            is AddEditRecurringTransactionEvent.OnFrequencyChanged -> _state.update { it.copy(frequency = event.frequency) }
            is AddEditRecurringTransactionEvent.OnStartDateChanged -> _state.update { it.copy(startDate = event.startDate) }
            is AddEditRecurringTransactionEvent.OnIsActiveChanged -> _state.update { it.copy(isActive = event.isActive) }
            is AddEditRecurringTransactionEvent.OnSaveRecurringTransaction -> saveRecurringTransaction()
        }
    }

    private fun loadRecurringTransaction(id: UUID) {
        viewModelScope.launch {
            recurringTransactionUseCases.getRecurringTransactionById(id)?.let { recurringTransaction ->
                _state.update {
                    it.copy(
                        id = recurringTransaction.id,
                        name = recurringTransaction.name,
                        amount = recurringTransaction.amount.formatThousand(),
                        type = recurringTransaction.type,
                        category = recurringTransaction.category,
                        wallet = recurringTransaction.wallet,
                        note = recurringTransaction.note ?: "",
                        frequency = recurringTransaction.frequency,
                        startDate = recurringTransaction.startDate,
                        isActive = recurringTransaction.isActive
                    )
                }
            }
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                categoryUseCases.getAllCategories(),
                walletUseCases.getAllWallets()
            ) { categories, wallets ->
                _state.update { state ->
                    val filteredCategories = categories.filter {
                        it.type == when (state.type) {
                            dev.muffar.moneyfikasi.domain.model.TransactionType.INCOME -> CategoryType.INCOME
                            else -> CategoryType.EXPENSE
                        }
                    }
                    state.copy(
                        categories = filteredCategories,
                        wallets = wallets
                    )
                }
            }.collectLatest {}
        }
    }

    private fun saveRecurringTransaction() {
        viewModelScope.launch {
            recurringTransactionUseCases.saveRecurringTransaction(_state.value.recurringTransaction)
            _eventFlow.send(UiEvent.SaveRecurringTransaction)
        }
    }

    sealed class UiEvent {
        object SaveRecurringTransaction : UiEvent()
    }
}
