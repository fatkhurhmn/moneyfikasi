package dev.muffar.moneyfikasi.wallet.add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.InvalidWalletException
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.utils.clearThousandFormat
import dev.muffar.moneyfikasi.utils.formatThousand
import dev.muffar.moneyfikasi.wallet.add_edit.component.AddEditWalletBottomSheet
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
            is AddEditWalletEvent.OnNameChange -> onNameChange(event.name)
            is AddEditWalletEvent.OnBalanceChange -> onBalanceChange(event.balance)
            is AddEditWalletEvent.OnIconChange -> onIconChange(event.icon)
            is AddEditWalletEvent.OnColorChange -> onColorChange(event.color)
            is AddEditWalletEvent.OnIsActiveChange -> onIsActiveChange()
            is AddEditWalletEvent.OnBottomSheetChange -> onBottomSheetChange(event.type)
            is AddEditWalletEvent.OnShowAlert -> onShowAlert(event.showAlert)
            is AddEditWalletEvent.OnSubmitWallet -> onSubmitWallet()
            is AddEditWalletEvent.OnDeleteWallet -> onDeleteWallet()
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
                            balance = it.balance.toLong().formatThousand(),
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

    private fun onBalanceChange(balance: String) {
        _state.update { it.copy(balance = balance) }
    }

    private fun onIconChange(icon: String) {
        _state.update { it.copy(icon = icon) }
    }

    private fun onColorChange(color: Long) {
        _state.update { it.copy(color = color) }
    }

    private fun onIsActiveChange() {
        val id = _state.value.id
        val isActive = !_state.value.isActive
        viewModelScope.launch {
            walletUseCases.updateWalletActivation(id!!, isActive)
        }
        _state.update { it.copy(isActive = isActive) }
    }

    private fun onBottomSheetChange(type: AddEditWalletBottomSheet?) {
        _state.update { it.copy(bottomSheetType = type) }
    }

    private fun onShowAlert(showAlert: Boolean) {
        _state.update { it.copy(showAlert = showAlert) }
    }

    private fun onSubmitWallet() {
        viewModelScope.launch {
            try {
                val wallet = Wallet(
                    id = state.value.id ?: UUID.randomUUID(),
                    name = state.value.name,
                    balance = state.value.balance.clearThousandFormat().toDouble(),
                    icon = state.value.icon,
                    color = state.value.color,
                    isActive = state.value.isActive
                )
                walletUseCases.saveWallet(wallet)
                _eventFlow.emit(UiEvent.SaveWallet)
            } catch (e: InvalidWalletException) {
                _eventFlow.emit(UiEvent.ShowMessage(e.message))
            }
        }
    }

    private fun onDeleteWallet() {
        viewModelScope.launch {
            try {
                walletUseCases.deleteWallet(state.value.id!!)
                _eventFlow.emit(UiEvent.DeleteWallet)
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowMessage(e.message ?: "Error deleting wallet"))
            }
        }
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String) : UiEvent()
        data object SaveWallet : UiEvent()
        data object DeleteWallet : UiEvent()
    }
}