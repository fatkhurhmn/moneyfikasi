package dev.muffar.moneyfikasi.debt.add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.usecase.debt.DebtUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.constants.UUIDConst
import dev.muffar.moneyfikasi.utils.constants.ValidationConst
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import dev.muffar.moneyfikasi.utils.extensions.StringExt.clearThousandFormat
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditDebtViewModel @Inject constructor(
    private val debtUseCases: DebtUseCases,
    private val walletUseCases: WalletUseCases,
    private val handle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditDebtState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        initState()
        loadWallets()
    }

    fun onEvent(event: AddEditDebtEvent) {
        when (event) {
            is AddEditDebtEvent.TypeChanged -> onTypeChange(event.type)
            is AddEditDebtEvent.NameChanged -> onNameChange(event.name)
            is AddEditDebtEvent.TotalAmountChanged -> onTotalAmountChange(event.amount)
            is AddEditDebtEvent.PaidAmountChanged -> onPaidAmountChange(event.amount)
            is AddEditDebtEvent.DebtDateChanged -> onDebtDateChange(event.date)
            is AddEditDebtEvent.DueDateChanged -> onDueDateChange(event.dueDate)
            is AddEditDebtEvent.NoteChanged -> onNoteChange(event.note)
            is AddEditDebtEvent.WalletSelected -> onWalletSelect(event.wallet)
            is AddEditDebtEvent.SaveDebt -> onSaveDebt()
        }
    }

    private fun initState() {
        handle.get<String>(Screen.AddEditDebt.TYPE)?.let { typeStr ->
            if (typeStr.isNotEmpty()) {
                _state.update { it.copy(type = DebtType.fromString(typeStr)) }
            }
        }
        handle.get<String>(Screen.AddEditDebt.DEBT_ID)?.let { debtId ->
            if (debtId.isNotEmpty()) {
                viewModelScope.launch {
                    debtUseCases.getDebtById(UUID.fromString(debtId))?.let { debt ->
                        _state.update {
                            it.copy(
                                id = debt.id,
                                name = debt.name,
                                type = debt.type,
                                totalAmount = debt.totalAmount.formatThousand(),
                                paidAmount = debt.paidAmount.formatThousand(),
                                dueDate = debt.dueDate,
                                note = debt.note,
                                transactionId = debt.transactionId,
                                createdAt = debt.createdAt
                            )
                        }
                        loadWallet(debt.walletId)
                    }
                }
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

    private fun loadWallet(walletId: UUID) {
        viewModelScope.launch {
            val wallet = if (walletId == UUIDConst.empty) {
                null
            } else {
                walletUseCases.getWalletById(walletId)
            }
            _state.update {
                it.copy(wallet = wallet ?: Wallet(id = UUIDConst.empty, name = "Unknown"))
            }
        }
    }

    private fun onTypeChange(type: DebtType) {
        _state.update { it.copy(type = type) }
    }

    private fun onNameChange(name: String) {
        if (name.length > ValidationConst.MAX_NAME_LENGTH) return
        _state.update { it.copy(name = name) }
        updateNameError()
    }

    private fun updateNameError() {
        val name = _state.value.name
        val error = if (name.isEmpty()) R.string.error_name_empty else null
        _state.update { it.copy(nameError = ErrorMessage(resId = error)) }
    }

    private fun onTotalAmountChange(amount: String) {
        if (amount.length > ValidationConst.MAX_AMOUNT_LENGTH) return
        _state.update { it.copy(totalAmount = amount) }
        updateTotalAmountError()
        updatePaidAmountError()
    }

    private fun updateTotalAmountError() {
        val amount = _state.value.totalAmount.clearThousandFormat().toDoubleOrNull() ?: 0.0
        val error = if (amount <= 0) R.string.error_amount_greater_than_zero else null
        _state.update { it.copy(totalAmountError = ErrorMessage(resId = error)) }
    }

    private fun onPaidAmountChange(amount: String) {
        if (amount.length > ValidationConst.MAX_AMOUNT_LENGTH) return
        _state.update { it.copy(paidAmount = amount) }
        updatePaidAmountError()
    }

    private fun updatePaidAmountError() {
        _state.update { it.copy(paidAmountError = ErrorMessage()) }
    }

    private fun onDueDateChange(dueDate: Long?) {
        _state.update { it.copy(dueDate = dueDate) }
        updateDueDateError()
    }

    private fun onDebtDateChange(date: Long) {
        _state.update {
            val dueDate = it.dueDate
            it.copy(
                createdAt = date,
                dueDate = if (dueDate != null && isBeforeDay(dueDate, date)) null else dueDate
            )
        }
        updateDueDateError()
    }

    private fun updateDueDateError() {
        val state = _state.value
        val dueDate = state.dueDate
        val error = when {
            dueDate == null -> R.string.error_due_date_required
            isBeforeDay(dueDate, state.createdAt) -> R.string.error_end_date_invalid
            else -> null
        }
        _state.update { it.copy(dueDateError = ErrorMessage(resId = error)) }
    }

    private fun isBeforeDay(first: Long, second: Long): Boolean {
        val zone = ZoneId.systemDefault()
        val firstDate = Instant.ofEpochMilli(first).atZone(zone).toLocalDate()
        val secondDate = Instant.ofEpochMilli(second).atZone(zone).toLocalDate()
        return firstDate.isBefore(secondDate)
    }

    private fun onNoteChange(note: String) {
        if (note.length > ValidationConst.MAX_NOTE_LENGTH) return
        _state.update { it.copy(note = note) }
    }

    private fun onWalletSelect(wallet: Wallet) {
        _state.update { it.copy(wallet = wallet) }
        updateWalletError()
    }

    private fun updateWalletError() {
        val wallet = _state.value.wallet
        val error = if (wallet.isNotSet) R.string.error_select_wallet else null
        _state.update { it.copy(walletError = ErrorMessage(resId = error)) }
    }

    private fun onSaveDebt() {
        if (!isFormValid()) return
        val state = _state.value
        viewModelScope.launch {
            try {
                debtUseCases.upsertDebt(state.debt)
                _eventFlow.emit(UiEvent.SaveDebt)
            } catch (e: Exception) {
                e.printStackTrace()
                _eventFlow.emit(
                    UiEvent.ShowMessage(
                        R.string.error_save_debt_failed,
                        SnackbarType.ERROR
                    )
                )
            }
        }
    }

    private fun isFormValid(): Boolean {
        updateNameError()
        updateTotalAmountError()
        updatePaidAmountError()
        updateWalletError()
        updateDueDateError()
        val state = _state.value
        return state.nameError.isNull && state.totalAmountError.isNull && state.paidAmountError.isNull && state.walletError.isNull && state.dueDateError.isNull
    }

    sealed class UiEvent {
        data class ShowMessage(val messageResId: Int, val type: SnackbarType) : UiEvent()
        data object SaveDebt : UiEvent()
    }
}
