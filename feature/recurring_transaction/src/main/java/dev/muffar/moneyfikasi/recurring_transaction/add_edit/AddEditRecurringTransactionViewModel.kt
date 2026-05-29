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
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.recurring_transaction.RecurringTransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.resource.R
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
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditRecurringTransactionViewModel @Inject constructor(
    private val recurringTransactionUseCases: RecurringTransactionUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val walletUseCases: WalletUseCases,
    handle: SavedStateHandle,
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
            is AddEditRecurringTransactionEvent.NameChanged -> onNameChange(event.name)
            is AddEditRecurringTransactionEvent.AmountChanged -> onAmountChange(event.amount)
            is AddEditRecurringTransactionEvent.TypeChanged -> onTypeChange(event.type, event.isInit)
            is AddEditRecurringTransactionEvent.CategoryChanged -> onCategoryChange(event.category)
            is AddEditRecurringTransactionEvent.WalletChanged -> onWalletChange(event.wallet)
            is AddEditRecurringTransactionEvent.FrequencyChanged -> onFrequencyChange(event.frequency)
            is AddEditRecurringTransactionEvent.StartDateChanged -> onStartDateChange(event.startDate)
            is AddEditRecurringTransactionEvent.StartTimeChanged -> onStartTimeChange(event.startTime)
            is AddEditRecurringTransactionEvent.EndTypeChanged -> onEndTypeChange(event.endType)
            is AddEditRecurringTransactionEvent.EndDateChanged -> onEndDateChange(event.endDate)
            is AddEditRecurringTransactionEvent.OccurrenceCountChanged -> onOccurrenceCountChange(event.count)
            is AddEditRecurringTransactionEvent.IsSkipFirstChanged -> onIsSkipFirstChange(event.isSkipFirst)
            is AddEditRecurringTransactionEvent.IsActiveChanged -> onIsActiveChange(event.isActive)
            is AddEditRecurringTransactionEvent.SaveRecurringTransaction -> onSaveRecurringTransaction()
            is AddEditRecurringTransactionEvent.DeleteRecurringTransaction -> onDeleteRecurringTransaction()
        }
    }

    private fun onNameChange(name: String) {
        if (name.length > ValidationConst.MAX_NAME_LENGTH) return
        _state.update { it.copy(name = name) }
        updateNameError()
    }

    private fun onAmountChange(amount: String) {
        if (amount.length > ValidationConst.MAX_AMOUNT_LENGTH) return
        _state.update { it.copy(amount = amount) }
        updateAmountError()
    }

    private fun onTypeChange(type: TransactionType, isInit: Boolean) {
        val category = if (isInit) _state.value.category else Category()
        _state.update { it.copy(type = type, category = category) }
    }

    private fun onCategoryChange(category: Category) {
        _state.update { it.copy(category = category) }
        updateCategoryError()
    }

    private fun onWalletChange(wallet: Wallet) {
        _state.update { it.copy(wallet = wallet) }
        updateWalletError()
    }

    private fun onFrequencyChange(frequency: TimePeriod) {
        _state.update { it.copy(frequency = frequency) }
    }

    private fun onStartDateChange(startDate: Long) {
        _state.update {
            it.copy(
                startDate = startDate,
                endDate = if (it.endDate < startDate) startDate else it.endDate,
                isSkipFirst = if (startDate != LocalDate.now()
                        .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
                ) false else it.isSkipFirst
            )
        }
    }

    private fun onStartTimeChange(startTime: Pair<Int, Int>) {
        _state.update { it.copy(startTime = startTime) }
    }

    private fun onEndTypeChange(endType: RecurringEndType) {
        _state.update { it.copy(endType = endType) }
    }

    private fun onEndDateChange(endDate: Long) {
        _state.update { it.copy(endDate = endDate) }
    }

    private fun onOccurrenceCountChange(count: String) {
        if (count.length > ValidationConst.MAX_OCCURRENCE_LENGTH) return
        if (count.all { it.isDigit() }) {
            _state.update { it.copy(occurrenceCount = count) }
            updateOccurrenceCountError()
        }
    }

    private fun onIsSkipFirstChange(isSkipFirst: Boolean) {
        _state.update { it.copy(isSkipFirst = isSkipFirst) }
    }

    private fun onIsActiveChange(isActive: Boolean) {
        _state.update { it.copy(isActive = isActive) }
    }

    private fun updateNameError() {
        val error = if (_state.value.name.isBlank()) R.string.error_name_empty else null
        _state.update { it.copy(nameError = ErrorMessage(resId = error)) }
    }

    private fun updateAmountError() {
        val amount = _state.value.amount.clearThousandFormat().toDoubleOrNull() ?: 0.0
        val error = if (amount <= 0) R.string.error_amount_greater_than_zero else null
        _state.update { it.copy(amountError = ErrorMessage(resId = error)) }
    }

    private fun updateCategoryError() {
        val category = _state.value.category
        val error = if (category.id == UUIDConst.empty) R.string.error_select_category else null
        _state.update { it.copy(categoryError = ErrorMessage(resId = error)) }
    }

    private fun updateWalletError() {
        val wallet = _state.value.wallet
        val error = if (wallet.id == UUIDConst.empty) R.string.error_select_wallet else null
        _state.update { it.copy(walletError = ErrorMessage(resId = error)) }
    }

    private fun updateOccurrenceCountError() {
        val count = _state.value.occurrenceCount.toIntOrNull() ?: 0
        val error = if (count <= 0) R.string.error_count_greater_than_zero else null
        _state.update { it.copy(occurrenceCountError = ErrorMessage(resId = error)) }
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
                    val nextRun = recurringTransaction.nextRun ?: recurringTransaction.startDate
                    val localDateTime = Instant.ofEpochMilli(nextRun)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()

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
                            startTime = localDateTime.hour to localDateTime.minute,
                            initialStartDate = recurringTransaction.startDate,
                            initialFrequency = recurringTransaction.frequency,
                            endType = recurringTransaction.endType,
                            initialEndType = recurringTransaction.endType,
                            endDate = recurringTransaction.endDate ?: it.endDate,
                            initialEndDate = recurringTransaction.endDate,
                            occurrenceCount = recurringTransaction.occurrenceCount?.toString()
                                ?: it.occurrenceCount,
                            initialOccurrenceCount = recurringTransaction.occurrenceCount,
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

    private fun onSaveRecurringTransaction() {
        if (!isFormValid()) return
        viewModelScope.launch {
            try {
                recurringTransactionUseCases.saveRecurringTransaction(_state.value.recurringTransaction)
                val hasActive = recurringTransactionUseCases.checkActiveRecurringTransactions()
                _eventFlow.emit(UiEvent.SaveRecurringTransaction(hasActive))
            } catch (_: Exception) {
                _eventFlow.emit(
                    UiEvent.ShowMessage(
                        R.string.error_save_recurring_transaction_failed,
                        SnackbarType.ERROR
                    )
                )
            }
        }
    }

    private fun onDeleteRecurringTransaction() {
        val id = _state.value.id ?: return
        viewModelScope.launch {
            try {
                recurringTransactionUseCases.deleteRecurringTransaction(id)
                val hasActive = recurringTransactionUseCases.checkActiveRecurringTransactions()
                _eventFlow.emit(UiEvent.DeleteRecurringTransaction(hasActive))
            } catch (_: Exception) {
                _eventFlow.emit(
                    UiEvent.ShowMessage(
                        R.string.error_delete_recurring_transaction_failed,
                        SnackbarType.ERROR
                    )
                )
            }
        }
    }

    sealed class UiEvent {
        data class SaveRecurringTransaction(val hasActive: Boolean) : UiEvent()
        data class DeleteRecurringTransaction(val hasActive: Boolean) : UiEvent()
        data class ShowMessage(val messageResId: Int, val type: SnackbarType) : UiEvent()
    }
}
