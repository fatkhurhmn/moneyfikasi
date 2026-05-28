package dev.muffar.moneyfikasi.preset.add_edit

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditPresetViewModel @Inject constructor(
    private val presetUseCases: PresetUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val walletUseCases: WalletUseCases,
    private val handle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditPresetState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        initState()
        loadWallets()
    }

    fun onEvent(event: AddEditPresetEvent) {
        when (event) {
            is AddEditPresetEvent.TypeChanged -> onTypeChange(event.type, event.isInit)
            is AddEditPresetEvent.NameChanged -> onNameChange(event.name)
            is AddEditPresetEvent.AmountChanged -> onAmountChange(event.amount)
            is AddEditPresetEvent.CategoryChanged -> onCategorySelect(event.category)
            is AddEditPresetEvent.WalletChanged -> onWalletSelect(event.wallet)
            is AddEditPresetEvent.SavePreset -> onSavePreset()
            is AddEditPresetEvent.DeletePreset -> onDeletePreset()
            is AddEditPresetEvent.ShowDeleteAlert -> onShowDeleteAlert(event.show)
        }
    }

    private fun initState() {
        handle.get<String?>(Screen.AddEditPreset.PRESET_ID)?.let { id ->
            if (id.isEmpty()) return
            viewModelScope.launch {
                presetUseCases.getPresetById(UUID.fromString(id))?.also {
                    _state.update { state ->
                        state.copy(
                            id = it.id,
                            name = it.name,
                            amount = it.amount?.formatThousand() ?: "0",
                            type = it.type,
                            category = it.category ?: Category(),
                            wallet = it.wallet ?: Wallet()
                        )
                    }
                    loadCategories()
                }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryUseCases.getCategoryByType(state.value.categoryType)
                .collectLatest { categories ->
                    _state.update { it.copy(categories = categories) }
                }
        }
    }

    private fun loadWallets() {
        viewModelScope.launch {
            walletUseCases.getAllWallets().collectLatest { wallets ->
                _state.update { it.copy(wallets = wallets) }
            }
        }
    }

    private fun onTypeChange(type: TransactionType, isInit: Boolean) {
        val category = if (isInit) _state.value.category else Category()
        _state.update { it.copy(type = type, category = category) }
        loadCategories()
    }

    private fun onNameChange(name: String) {
        if (name.length > ValidationConst.MAX_NAME_LENGTH) return
        _state.update { it.copy(name = name) }
        updateNameError()
    }

    private fun updateNameError() {
        val error = if (_state.value.name.isEmpty()) R.string.name_cannot_be_empty else null
        _state.update { it.copy(nameError = ErrorMessage(resId = error)) }
    }

    private fun onAmountChange(amount: String) {
        if (amount.length > ValidationConst.MAX_AMOUNT_LENGTH) return
        _state.update { it.copy(amount = amount) }
    }

    private fun onCategorySelect(category: Category?) {
        _state.update { it.copy(category = category ?: Category()) }
    }

    private fun onWalletSelect(wallet: Wallet?) {
        _state.update { it.copy(wallet = wallet ?: Wallet()) }
    }

    private fun onShowDeleteAlert(show: Boolean) {
        _state.update { it.copy(showAlert = show) }
    }

    private fun onSavePreset() {
        if (!isFormValid()) return
        val state = state.value
        viewModelScope.launch {
            try {
                presetUseCases.upsertPreset(state.preset)
                _eventFlow.emit(UiEvent.SavePreset)
            } catch (e: Exception) {
                e.printStackTrace()
                _eventFlow.emit(UiEvent.ShowMessage(R.string.failed_to_save_preset, SnackbarType.ERROR))
            }
        }
    }

    private fun onDeletePreset() {
        val state = state.value
        viewModelScope.launch {
            try {
                presetUseCases.deletePreset(state.preset)
                _eventFlow.emit(UiEvent.DeletePreset)
            } catch (e: Exception) {
                e.printStackTrace()
                _eventFlow.emit(UiEvent.ShowMessage(R.string.failed_to_delete_preset, SnackbarType.ERROR))
            }
        }
    }

    private fun isFormValid(): Boolean {
        updateNameError()
        val state = _state.value
        val isNameValid = state.nameError.isNull

        val amount = state.amount.clearThousandFormat().toDoubleOrNull() ?: 0.0
        val isAtLeastOneFilled = amount > 0 ||
                state.category.id != UUIDConst.empty ||
                state.wallet.id != UUIDConst.empty

        if (isNameValid && !isAtLeastOneFilled) {
            viewModelScope.launch {
                _eventFlow.emit(
                    UiEvent.ShowMessage(
                        R.string.preset_requires_value,
                        SnackbarType.ERROR
                    )
                )
            }
        }

        return isNameValid && isAtLeastOneFilled
    }

    sealed class UiEvent {
        data class ShowMessage(val messageResId: Int, val type: SnackbarType) : UiEvent()
        data object SavePreset : UiEvent()
        data object DeletePreset : UiEvent()
    }
}
