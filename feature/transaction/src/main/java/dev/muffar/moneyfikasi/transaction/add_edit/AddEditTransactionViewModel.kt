package dev.muffar.moneyfikasi.transaction.add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.preset.PresetUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.utils.constants.UUIDConst
import dev.muffar.moneyfikasi.utils.constants.ValidationConst
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.toMilliseconds
import dev.muffar.moneyfikasi.utils.extensions.LongExt.toFormattedDateTime
import dev.muffar.moneyfikasi.utils.extensions.StringExt.clearThousandFormat
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
    private val presetUseCases: PresetUseCases,
    private val handle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditTransactionState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        initState()
        loadWallets()
    }

    fun onEvent(event: AddEditTransactionEvent) {
        when (event) {
            is AddEditTransactionEvent.TypeChanged -> onTypeChange(event.type, event.isInit)
            is AddEditTransactionEvent.AmountChanged -> onAmountChange(event.amount)
            is AddEditTransactionEvent.CategorySelected -> onCategorySelect(event.category)
            is AddEditTransactionEvent.WalletSelected -> onWalletSelect(event.wallet)
            is AddEditTransactionEvent.DateSelected -> onDateSelect(event.date)
            is AddEditTransactionEvent.TimeSelected -> onTimeSelect(event.time)
            is AddEditTransactionEvent.NoteChanged -> onNoteChange(event.note)
            is AddEditTransactionEvent.SaveTransaction -> onSaveTransaction()
        }
    }

    private fun initState() {
        val transactionIdStr = handle.get<String>(Screen.AddEditTransaction.TRANSACTION_ID)
        val presetIdStr = handle.get<String>(Screen.AddEditTransaction.PRESET_ID)

        if (!transactionIdStr.isNullOrEmpty()) {
            val transactionId = UUID.fromString(transactionIdStr)
            populateTransaction(transactionId)
        } else if (!presetIdStr.isNullOrEmpty()) {
            val presetId = UUID.fromString(presetIdStr)
            populatePreset(presetId)
        }
    }

    private fun populateTransaction(transactionId: UUID) {
        viewModelScope.launch {
            transactionUseCases.getTransactionById(transactionId)?.let { transaction ->
                _state.update { state ->
                    val date = transaction.date.toMilliseconds()
                    state.copy(
                        id = transactionId,
                        type = transaction.type,
                        amount = transaction.amount.formatThousand(),
                        category = transaction.category,
                        wallet = transaction.wallet,
                        note = transaction.note ?: "",
                        date = date,
                        hour = date.toFormattedDateTime("H").toInt(),
                        minute = date.toFormattedDateTime("mm").toInt()
                    )
                }
                loadCategories()
            }
        }
    }

    private fun populatePreset(presetId: UUID) {
        viewModelScope.launch {
            presetUseCases.getPresetById(presetId)?.let { preset ->
                _state.update { state ->
                    state.copy(
                        type = preset.type,
                        amount = preset.amount?.formatThousand() ?: "0",
                        category = preset.category ?: Category(),
                        wallet = preset.wallet ?: Wallet(),
                        note = preset.description ?: ""
                    )
                }
                loadCategories()
            }
        }
    }

    private fun onTypeChange(type: TransactionType, isInit: Boolean) {
        val category = if (isInit) _state.value.category else Category()
        _state.update {
            it.copy(
                type = type,
                category = category
            )
        }
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryUseCases.getCategoryByType(state.value.categoryType)
                .collectLatest { categories ->
                    val filteredCategories = categories.filter { it.isActive }
                    _state.update { it.copy(categoryOptions = filteredCategories) }
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
        if (amount.length > ValidationConst.MAX_AMOUNT_LENGTH) return
        _state.update { it.copy(amount = amount) }
        updateAmountError()
    }

    private fun updateAmountError() {
        val amount = state.value.amount
        val error = when {
            amount.isEmpty() -> "Amount cannot be empty"
            amount.clearThousandFormat().toDouble() == 0.0 -> "Minimum amount is 1"
            else -> null
        }
        _state.update { it.copy(amountError = ErrorMessage(error)) }
    }

    private fun onNoteChange(note: String) {
        if (note.length > ValidationConst.MAX_NOTE_LENGTH) return
        _state.update { it.copy(note = note) }
    }

    private fun onCategorySelect(category: Category) {
        _state.update { it.copy(category = category) }
        updateCategoryError()
    }

    private fun updateCategoryError() {
        val category = state.value.category
        val error = if (category.id == UUIDConst.empty) "Category cannot be empty" else null
        _state.update { it.copy(categoryError = ErrorMessage(error)) }
    }

    private fun onWalletSelect(wallet: Wallet) {
        _state.update { it.copy(wallet = wallet) }
        updateWalletError()
    }

    private fun updateWalletError() {
        val wallet = state.value.wallet
        val error = if (wallet.isNotSet) "Wallet cannot be empty" else null
        _state.update { it.copy(walletError = ErrorMessage(error)) }
    }

    private fun onDateSelect(date: Long) {
        _state.update { it.copy(date = date) }
    }

    private fun onTimeSelect(time: Pair<Int, Int>) {
        _state.update { it.copy(hour = time.first, minute = time.second) }
    }

    private fun onSaveTransaction() {
        val state = state.value
        if (!isFormValid()) return
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
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowMessage(e.message ?: "", SnackbarType.ERROR))
            }
        }
    }

    private fun isFormValid(): Boolean {
        viewModelScope.launch {
            updateAmountError()
            updateCategoryError()
            updateWalletError()
        }
        return state.value.run {
            categoryError.isNull && walletError.isNull && amountError.isNull
        }
    }

    private fun getFormattedDate(): LocalDateTime {
        return LocalDateTime
            .ofInstant(Instant.ofEpochMilli(state.value.date), ZoneId.systemDefault())
            .withHour(state.value.hour)
            .withMinute(state.value.minute)
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String, val type: SnackbarType) : UiEvent()
        data object SaveTransaction : UiEvent()
        data object DeleteTransaction : UiEvent()
    }
}
