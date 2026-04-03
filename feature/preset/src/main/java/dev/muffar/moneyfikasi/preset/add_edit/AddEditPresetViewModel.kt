package dev.muffar.moneyfikasi.preset.add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.Preset
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.preset.PresetUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
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
        loadCategories()
        loadWallets()
    }

    fun onEvent(event: AddEditPresetEvent) {
        when (event) {
            is AddEditPresetEvent.NameChanged -> onNameChange(event.name)
            is AddEditPresetEvent.AmountChanged -> onAmountChange(event.amount)
            is AddEditPresetEvent.CategoryChanged -> onCategorySelect(event.category)
            is AddEditPresetEvent.WalletChanged -> onWalletSelect(event.wallet)
            is AddEditPresetEvent.NoteChanged -> onNoteChange(event.note)
            is AddEditPresetEvent.SavePreset -> onSavePreset()
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
                            amount = it.amount.toString(),
                            type = it.type,
                            category = it.category,
                            wallet = it.wallet,
                            note = it.note ?: ""
                        )
                    }
                }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryUseCases.getAllCategories(true).collectLatest { categories ->
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

    private fun onNameChange(name: String) {
        if (name.length > 50) return
        _state.update { it.copy(name = name) }
        updateNameError()
    }

    private fun updateNameError() {
        val error = if (_state.value.name.isEmpty()) "Name cannot be empty" else null
        _state.update { it.copy(nameError = ErrorMessage(error)) }
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
        _state.update { it.copy(category = category) }
    }

    private fun onWalletSelect(wallet: Wallet) {
        _state.update { it.copy(wallet = wallet) }
    }

    private fun onSavePreset() {
        if (!isFormValid()) return
        viewModelScope.launch {
            try {
                val preset = Preset(
                    id = _state.value.id ?: UUID.randomUUID(),
                    name = _state.value.name,
                    amount = _state.value.amount.toDouble(),
                    type = _state.value.type,
                    category = _state.value.category,
                    wallet = _state.value.wallet,
                    note = _state.value.note.ifEmpty { null }
                )
                presetUseCases.upsertPreset(preset)
                _eventFlow.emit(UiEvent.SavePreset)
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowMessage("Failed to save preset", SnackbarType.ERROR))
            }
        }
    }

    private fun isFormValid(): Boolean {
        updateNameError()
        return _state.value.nameError.isNull
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String, val type: SnackbarType) : UiEvent()
        data object SavePreset : UiEvent()
    }
}
