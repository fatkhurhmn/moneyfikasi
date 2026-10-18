package dev.muffar.moneyfikasi.debt.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.debt.DebtUseCases
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebtMenuViewModel @Inject constructor(
    private val debtUseCases: DebtUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(DebtMenuState())
    val state: StateFlow<DebtMenuState> = _state.asStateFlow()

    init {
        loadDebts()
    }

    private fun loadDebts() {
        viewModelScope.launch {
            debtUseCases.getAllDebts().collectLatest { debts ->
                _state.update { it.copy(debts = debts) }
            }
        }
    }
}
