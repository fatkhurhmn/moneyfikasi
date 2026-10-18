package dev.muffar.moneyfikasi.debt.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.usecase.debt.DebtUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebtsViewModel @Inject constructor(
    private val debtUseCases: DebtUseCases,
    private val handle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DebtsState())
    val state: StateFlow<DebtsState> = _state.asStateFlow()

    init {
        initType()
        loadDebts()
    }

    fun onEvent(event: DebtsEvent) {
        when (event) {
            is DebtsEvent.StatusFilterChanged -> onStatusFilterChange(event.filter)
        }
    }

    private fun initType() {
        handle.get<String>(Screen.Debts.TYPE)?.let { typeStr ->
            if (typeStr.isNotEmpty()) {
                _state.update { it.copy(selectedType = DebtType.fromString(typeStr)) }
            }
        }
    }

    private fun loadDebts() {
        viewModelScope.launch {
            debtUseCases.getAllDebts().collectLatest { debts ->
                _state.update { it.copy(debts = debts) }
            }
        }
    }

    private fun onStatusFilterChange(filter: DebtStatusFilter) {
        _state.update { it.copy(statusFilter = filter) }
    }
}
