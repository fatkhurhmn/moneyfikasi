package dev.muffar.moneyfikasi.transaction.add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.InvalidTransactionException
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.utils.extensions.clearThousandFormat
import dev.muffar.moneyfikasi.utils.extensions.formatThousand
import dev.muffar.moneyfikasi.utils.extensions.toFormattedDateTime
import dev.muffar.moneyfikasi.utils.extensions.toMilliseconds
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val walletUseCases: WalletUseCases,
    private val handle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditTransactionState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        initState()
    }

    fun onEvent(event: AddEditTransactionEvent) {
        when (event) {
            is AddEditTransactionEvent.OnInitType -> setType(event.type)
            is AddEditTransactionEvent.OnAmountChange -> onAmountChange(event.amount)
            is AddEditTransactionEvent.OnCategorySelect -> onCategorySelect(event.category)
            is AddEditTransactionEvent.OnWalletSelect -> onWalletSelect(event.wallet)
            is AddEditTransactionEvent.OnDateSelect -> onDateSelect(event.date)
            is AddEditTransactionEvent.OnTimeSelect -> onTimeSelect(event.time)
            is AddEditTransactionEvent.OnNoteChange -> onNoteChange(event.note)
            is AddEditTransactionEvent.OnCreateClicked -> onSaveTransaction()
        }
    }

    private fun initState() {
        handle.get<String>(Screen.AddEditTransaction.TRANSACTION_ID)?.let { id ->
            if (id.isEmpty()) return
            val transactionId = UUID.fromString(id)
            viewModelScope.launch {
                transactionUseCases.getTransactionById(transactionId)?.let { transaction ->
                    _state.update { state ->
                        val date = transaction.date.toMilliseconds()
                        with(transaction) {
                            state.copy(
                                id = transactionId,
                                type = type,
                                amount = amount.formatThousand(),
                                category = category,
                                wallet = wallet,
                                note = note ?: "",
                                date = date,
                                hour = date.toFormattedDateTime("H").toInt(),
                                minute = date.toFormattedDateTime("mm").toInt()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setType(type: TransactionType) {
        _state.update { it.copy(type = type) }
        loadCategories()
        loadWallets()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryUseCases.getAllCategories().collectLatest { categories ->
                val filteredCategories = categories
                    .filter { it.type.name == state.value.type.name }
                    .filter { it.isActive }
                _state.update { it.copy(categories = filteredCategories) }
            }
        }
    }

    private fun loadWallets() {
        viewModelScope.launch {
            walletUseCases.getAllWallets().collectLatest { wallets ->
                val activeWallets = wallets.filter { it.isActive }
                _state.update { it.copy(walletOptions = activeWallets) }
            }
        }
    }

    private fun onAmountChange(amount: String) {
        if (amount.length > 17) return
        _state.update { it.copy(amount = amount) }
    }

    private fun onNoteChange(note: String) {
        if (note.length > 255) return
        _state.update { it.copy(note = note) }
    }

    private fun onCategorySelect(category: Category) {
        _state.update {
            it.copy(category = category)
        }
    }

    private fun onWalletSelect(wallet: Wallet) {
        _state.update {
            it.copy(wallet = wallet)
        }
    }

    private fun onDateSelect(date: Long) {
        _state.update { it.copy(date = date) }
    }

    private fun onTimeSelect(time: Pair<Int, Int>) {
        _state.update { it.copy(hour = time.first, minute = time.second) }
    }

    private fun onSaveTransaction() {
        val state = this.state.value
        viewModelScope.launch {
            try {
                if (state.isEditMode) {
                    transactionUseCases.updateTransaction(
                        id = state.id!!,
                        amount = state.amount.clearThousandFormat().toDouble(),
                        note = state.note.trim(),
                        type = state.type,
                        categoryId = state.category.id,
                        walletId = state.wallet.id,
                        date = getFormattedDate(),
                    )
                } else {
                    transactionUseCases.addTransaction(
                        amount = state.amount.clearThousandFormat().toDouble(),
                        note = state.note.trim(),
                        type = state.type,
                        categoryId = state.category.id,
                        walletId = state.wallet.id,
                        date = getFormattedDate(),
                    )
                }
                _eventFlow.emit(UiEvent.SaveTransaction)
            } catch (e: InvalidTransactionException) {
                _eventFlow.emit(UiEvent.ShowMessage(e.message))
            }
        }
    }

    private fun getFormattedDate(): LocalDateTime {
        return LocalDateTime
            .ofInstant(Instant.ofEpochMilli(state.value.date), ZoneId.systemDefault())
            .withHour(state.value.hour)
            .withMinute(state.value.minute)
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String) : UiEvent()
        data object SaveTransaction : UiEvent()
        data object DeleteTransaction : UiEvent()
    }
}