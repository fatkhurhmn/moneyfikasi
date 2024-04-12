package dev.muffar.moneyfikasi.transaction.add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.InvalidTransactionException
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.transaction.add_edit.component.AddEditTransactionSheetType
import dev.muffar.moneyfikasi.utils.clearThousandFormat
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
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

    fun onEvent(event: AddEditTransactionEvent) {
        when (event) {
            is AddEditTransactionEvent.OnInitType -> setType(event.type)
            is AddEditTransactionEvent.OnAmountChange -> onAmountChange(event.amount)
            is AddEditTransactionEvent.OnCategorySelect -> onCategorySelect(event.category)
            is AddEditTransactionEvent.OnWalletSelect -> onWalletSelect(event.wallet)
            is AddEditTransactionEvent.OnDateSelect -> onDateSelect(event.date)
            is AddEditTransactionEvent.OnTimeSelect -> onTimeSelect(event.hour, event.minute)
            is AddEditTransactionEvent.OnDescriptionChange -> onDescriptionChange(event.description)
            is AddEditTransactionEvent.OnSaveClick -> onSaveClick()
            is AddEditTransactionEvent.OnDeleteClick -> onDeleteClick()
            is AddEditTransactionEvent.OnBottomSheetChange -> onBottomSheetChange(event.type)
        }
    }

    private fun setType(type: TransactionType) {
        _state.update { it.copy(type = type) }
        viewModelScope.launch {
            categoryUseCases.getAllCategories().collectLatest { categories ->
                val filteredCategories = categories.filter { it.type.name == type.name }
                _state.update { it.copy(categories = filteredCategories) }
            }
        }

        viewModelScope.launch {
            walletUseCases.getAllWallets().collectLatest { wallets ->
                _state.update { it.copy(wallets = wallets) }
            }
        }
    }

    private fun onAmountChange(amount: String) {
        _state.update { it.copy(amount = amount) }
    }

    private fun onDescriptionChange(description: String) {
        _state.update { it.copy(description = description) }
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

    private fun onTimeSelect(hour: Int, minute: Int) {
        _state.update { it.copy(hour = hour, minute = minute) }
    }

    private fun onSaveClick() {
        viewModelScope.launch {
            try {
                val date = LocalDateTime
                    .ofEpochSecond(
                        state.value.date / 1000,
                        0,
                        ZoneOffset.UTC
                    )
                    .plusHours(state.value.hour.toLong())
                    .plusMinutes(state.value.minute.toLong())

                val transaction = Transaction(
                    id = state.value.id ?: UUID.randomUUID(),
                    amount = state.value.amount.clearThousandFormat().toDouble(),
                    description = state.value.description,
                    type = state.value.type,
                    category = state.value.category,
                    wallet = state.value.wallet,
                    date = date,
                )
                transactionUseCases.saveTransaction(transaction)
                _eventFlow.emit(UiEvent.SaveTransaction)
            } catch (e: InvalidTransactionException) {
                _eventFlow.emit(UiEvent.ShowMessage(e.message))
            }

        }
    }

    private fun onDeleteClick() {

    }

    private fun onBottomSheetChange(type: AddEditTransactionSheetType?) {
        _state.update { it.copy(bottomSheetType = type) }
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String) : UiEvent()
        data object SaveTransaction : UiEvent()
        data object DeleteTransaction : UiEvent()
    }
}