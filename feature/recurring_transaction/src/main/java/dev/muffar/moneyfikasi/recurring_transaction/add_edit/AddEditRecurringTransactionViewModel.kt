package dev.muffar.moneyfikasi.recurring_transaction.add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.recurring_transaction.RecurringTransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.utils.constants.UUIDConst
import dev.muffar.moneyfikasi.utils.constants.ValidationConst
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import dev.muffar.moneyfikasi.utils.extensions.StringExt.clearThousandFormat
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditRecurringTransactionViewModel @Inject constructor(
    private val recurringTransactionUseCases: RecurringTransactionUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val walletUseCases: WalletUseCases,
    private val handle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditRecurringTransactionState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        val recurringTransactionId =
            handle.get<String?>(Screen.AddEditRecurringTransaction.RECURRING_TRANSACTION_ID)
        if (!recurringTransactionId.isNullOrEmpty()) {
            loadRecurringTransaction(UUID.fromString(recurringTransactionId))
        }
        observeData()
    }

    fun onEvent(event: AddEditRecurringTransactionEvent) {
        when (event) {
            is AddEditRecurringTransactionEvent.OnNameChanged -> onNameChanged(event.name)
            is AddEditRecurringTransactionEvent.OnAmountChanged -> onAmountChanged(event.amount)
            is AddEditRecurringTransactionEvent.OnTypeChanged -> onTypeChanged(event.type, event.isInit)
            is AddEditRecurringTransactionEvent.OnCategoryChanged -> onCategoryChanged(event.category)
            is AddEditRecurringTransactionEvent.OnWalletChanged -> onWalletChanged(event.wallet)
            is AddEditRecurringTransactionEvent.OnFrequencyChanged -> _state.update {
                it.copy(
                    frequency = event.frequency
                )
            }

            is AddEditRecurringTransactionEvent.OnStartDateChanged -> {
                _state.update {
                    it.copy(
                        startDate = event.startDate,
                        endDate = if (it.endDate < event.startDate) event.startDate else it.endDate,
                        isSkipFirst = if (event.startDate != LocalDate.now()
                                .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
                        ) false else it.isSkipFirst
                    )
                }
            }

            is AddEditRecurringTransactionEvent.OnEndTypeChanged -> {
                _state.update { it.copy(endType = event.endType) }
            }

            is AddEditRecurringTransactionEvent.OnEndDateChanged -> {
                _state.update { it.copy(endDate = event.endDate) }
            }

            is AddEditRecurringTransactionEvent.OnOccurrenceCountChanged -> {
                if (event.count.all { it.isDigit() }) {
                    _state.update {
                        it.copy(
                            occurrenceCount = event.count
                        )
                    }
                    updateOccurrenceCountError()
                }
            }

            is AddEditRecurringTransactionEvent.OnIsSkipFirstChanged -> {
                _state.update {
                    it.copy(
                        isSkipFirst = event.isSkipFirst
                    )
                }
            }

            is AddEditRecurringTransactionEvent.OnIsActiveChanged -> {
                _state.update {
                    it.copy(
                        isActive = event.isActive
                    )
                }
            }

            is AddEditRecurringTransactionEvent.OnSaveRecurringTransaction -> saveRecurringTransaction()
            is AddEditRecurringTransactionEvent.OnDeleteRecurringTransaction -> deleteRecurringTransaction()
        }
    }

    private fun onNameChanged(name: String) {
        if (name.length > ValidationConst.MAX_NAME_LENGTH) return
        _state.update { it.copy(name = name) }
        updateNameError()
    }

    private fun onAmountChanged(amount: String) {
        if (amount.length > ValidationConst.MAX_AMOUNT_LENGTH) return
        _state.update { it.copy(amount = amount) }
        updateAmountError()
    }

    private fun onTypeChanged(type: TransactionType, isInit: Boolean) {
        val category = if (isInit) _state.value.category else Category()
        _state.update { it.copy(type = type, category = category) }
    }

    private fun onCategoryChanged(category: Category) {
        _state.update { it.copy(category = category) }
        updateCategoryError()
    }

    private fun onWalletChanged(wallet: Wallet) {
        _state.update { it.copy(wallet = wallet) }
        updateWalletError()
    }

    private fun updateNameError() {
        val error = if (_state.value.name.isBlank()) "Name cannot be empty" else null
        _state.update { it.copy(nameError = ErrorMessage(error)) }
    }

    private fun updateAmountError() {
        val amount = _state.value.amount.clearThousandFormat().toDoubleOrNull() ?: 0.0
        val error = if (amount <= 0) "Amount must be greater than 0" else null
        _state.update { it.copy(amountError = ErrorMessage(error)) }
    }

    private fun updateCategoryError() {
        val category = _state.value.category
        val error = if (category.id == UUIDConst.empty) "Please select a category" else null
        _state.update { it.copy(categoryError = ErrorMessage(error)) }
    }

    private fun updateWalletError() {
        val wallet = _state.value.wallet
        val error = if (wallet.id == UUIDConst.empty) "Please select a wallet" else null
        _state.update { it.copy(walletError = ErrorMessage(error)) }
    }

    private fun updateOccurrenceCountError() {
        val count = _state.value.occurrenceCount.toIntOrNull() ?: 0
        val error = if (count <= 0) "Count must be greater than 0" else null
        _state.update { it.copy(occurrenceCountError = ErrorMessage(error)) }
    }

    private fun isFormValid(): Boolean {
        updateNameError()
        updateAmountError()
        updateCategoryError()
        updateWalletError()
        updateOccurrenceCountError()

        return _state.value.nameError.isNull &&
                _state.value.amountError.isNull &&
                _state.value.categoryError.isNull &&
                _state.value.walletError.isNull &&
                (_state.value.endType != RecurringEndType.AFTER_OCCURRENCES || _state.value.occurrenceCountError.isNull)
    }

    private fun loadRecurringTransaction(id: UUID) {
        viewModelScope.launch {
            recurringTransactionUseCases.getRecurringTransactionById(id)
                ?.let { recurringTransaction ->
                    _state.update {
                        it.copy(
                            id = recurringTransaction.id,
                            name = recurringTransaction.name,
                            amount = recurringTransaction.amount.formatThousand(),
                            type = recurringTransaction.type,
                            category = recurringTransaction.category ?: Category(),
                            wallet = recurringTransaction.wallet ?: Wallet(),
                            frequency = recurringTransaction.frequency,
                            startDate = recurringTransaction.startDate,
                            initialStartDate = recurringTransaction.startDate,
                            endType = recurringTransaction.endType,
                            endDate = recurringTransaction.endDate ?: it.endDate,
                            occurrenceCount = recurringTransaction.occurrenceCount?.toString()
                                ?: it.occurrenceCount,
                            lastRun = recurringTransaction.lastRun,
                            nextRun = recurringTransaction.nextRun,
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
                walletUseCases.getAllWallets(),
                _state.map { it.type }.distinctUntilChanged()
            ) { categories, wallets, type ->
                val filteredCategories = categories.filter {
                    it.type == when (type) {
                        TransactionType.INCOME -> CategoryType.INCOME
                        else -> CategoryType.EXPENSE
                    }
                }
                filteredCategories to wallets
            }.collectLatest { (categories, wallets) ->
                _state.update { it.copy(categories = categories, wallets = wallets) }
            }
        }
    }

    private fun saveRecurringTransaction() {
        if (!isFormValid()) return
        viewModelScope.launch {
            try {
                recurringTransactionUseCases.saveRecurringTransaction(_state.value.recurringTransaction)
                _eventFlow.emit(UiEvent.SaveRecurringTransaction)
            } catch (e: Exception) {
                _eventFlow.emit(
                    UiEvent.ShowMessage(
                        "Failed to save recurring transaction",
                        SnackbarType.ERROR
                    )
                )
            }
        }
    }

    private fun deleteRecurringTransaction() {
        val id = _state.value.id ?: return
        viewModelScope.launch {
            try {
                recurringTransactionUseCases.deleteRecurringTransaction(id)
                _eventFlow.emit(UiEvent.DeleteRecurringTransaction)
            } catch (e: Exception) {
                _eventFlow.emit(
                    UiEvent.ShowMessage(
                        "Failed to delete recurring transaction",
                        SnackbarType.ERROR
                    )
                )
            }
        }
    }

    sealed class UiEvent {
        data object SaveRecurringTransaction : UiEvent()
        data object DeleteRecurringTransaction : UiEvent()
        data class ShowMessage(val message: String, val type: SnackbarType) : UiEvent()
    }
}
