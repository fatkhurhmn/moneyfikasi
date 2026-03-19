package dev.muffar.moneyfikasi.wallet.add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.utils.extensions.formatThousand
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditWalletViewModel @Inject constructor(
    private val walletUseCases: WalletUseCases,
    private val handle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditWalletState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        initState()
    }

    fun onEvent(event: AddEditWalletEvent) {
        when (event) {
            is AddEditWalletEvent.NameChanged -> onNameChange(event.name)
            is AddEditWalletEvent.BalanceChanged -> onBalanceChange(event.balance)
            is AddEditWalletEvent.IconChanged -> onIconChange(event.icon)
            is AddEditWalletEvent.ColorChanged -> onColorChange(event.color)
            is AddEditWalletEvent.WalletActivated -> onWalletActive()
            is AddEditWalletEvent.ShowDeleteAlert -> onShowAlert(event.showAlert)
            is AddEditWalletEvent.SaveWallet -> onSaveWallet()
            is AddEditWalletEvent.DeleteWallet -> onDeleteWallet()
        }
    }

    private fun initState() {
        handle.get<String?>(Screen.AddEditWallet.WALLET_ID)?.let { id ->
            if (id.isEmpty()) return
            viewModelScope.launch {
                walletUseCases.getWalletById(UUID.fromString(id))?.also {
                    _state.update { state ->
                        state.copy(
                            id = it.id,
                            name = it.name,
                            balance = it.balance.formatThousand(),
                            icon = it.icon,
                            color = it.color,
                            isActive = it.isActive
                        )
                    }
                }
            }
        }
    }

    private fun onNameChange(name: String) {
        _state.update { it.copy(name = name) }
    }

    private fun updateNameError() {
        val name = _state.value.name
        val error = if (name.isEmpty()) "Name cannot be empty" else null
        _state.update { it.copy(nameError = ErrorMessage(error)) }
    }

    private fun onBalanceChange(balance: String) {
        _state.update { it.copy(balance = balance) }
    }

    private fun onIconChange(icon: String) {
        _state.update { it.copy(icon = icon) }
    }

    private fun onColorChange(color: Long) {
        _state.update { it.copy(color = color) }
    }

    private fun updateIconAndColor() {
        val icon = _state.value.icon
        val color = _state.value.color
        val error = if (icon.isEmpty() || color == 0L) "Please select an icon and color" else null
        _state.update { it.copy(iconError = ErrorMessage(error)) }
    }

    private fun onWalletActive() {
        val isActive = !_state.value.isActive
        _state.update { it.copy(isActive = isActive) }
        viewModelScope.launch {
            walletUseCases.upsertWallet(state.value.wallet)
        }
    }

    private fun onShowAlert(showAlert: Boolean) {
        _state.update { it.copy(showAlert = showAlert) }
    }

    private fun onSaveWallet() {
        if (!isFormValid()) return
        viewModelScope.launch {
            try {
                walletUseCases.upsertWallet(state.value.wallet)
                _eventFlow.emit(UiEvent.SaveWallet)
            } catch (e: Exception) {
                e.printStackTrace()
                _eventFlow.emit(
                    UiEvent.ShowMessage(
                        "Failed to save wallet",
                        SnackbarType.ERROR
                    )
                )
            }
        }
    }

    private fun onDeleteWallet() {
        viewModelScope.launch {
            try {
                walletUseCases.deleteWallet(state.value.wallet)
                _eventFlow.emit(UiEvent.DeleteWallet)
            } catch (e: Exception) {
                e.printStackTrace()
                _eventFlow.emit(
                    UiEvent.ShowMessage(
                        "Failed to delete wallet",
                        SnackbarType.ERROR
                    )
                )
            }
        }
    }

    private fun isFormValid(): Boolean {
        viewModelScope.launch {
            updateNameError()
            updateIconAndColor()
        }
        return _state.value.run { nameError.isNull && iconError.isNull }
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String, val type: SnackbarType) : UiEvent()
        data object SaveWallet : UiEvent()
        data object DeleteWallet : UiEvent()
    }
}