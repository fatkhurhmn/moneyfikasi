package dev.muffar.moneyfikasi.budget.list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.budget.BudgetUseCases
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetsViewModel @Inject constructor(
    private val budgetUseCases: BudgetUseCases
) : ViewModel() {

    private val _state = mutableStateOf(BudgetsState())
    val state: State<BudgetsState> = _state

    init {
        getBudgets()
    }

    fun onEvent(event: BudgetsEvent) {
        when (event) {
            is BudgetsEvent.DeleteBudget -> {
                viewModelScope.launch {
                    budgetUseCases.deleteBudget(event.budget)
                }
            }
        }
    }

    private fun getBudgets() {
        budgetUseCases.getAllBudgets()
            .onEach { budgets ->
                _state.value = state.value.copy(
                    budgets = budgets
                )
            }.launchIn(viewModelScope)
    }
}
