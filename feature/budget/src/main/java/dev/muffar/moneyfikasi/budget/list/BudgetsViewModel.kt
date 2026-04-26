package dev.muffar.moneyfikasi.budget.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.budget.BudgetUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfMonth
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfMonth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BudgetsViewModel @Inject constructor(
    private val walletUseCases: WalletUseCases,
    private val budgetUseCases: BudgetUseCases,
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(BudgetsState())
    val state: StateFlow<BudgetsState> = _state.asStateFlow()

    init {
        loadWallets()
        loadBudgets()
        observeBudgets()
    }

    private fun loadWallets() {
        viewModelScope.launch {
            walletUseCases.getAllWallets().collectLatest { wallets ->
                _state.update { it.copy(wallets = wallets) }
            }
        }
    }

    private fun loadBudgets() {
        viewModelScope.launch {
            budgetUseCases.getAllBudgets().collectLatest { budgets ->
                _state.update { it.copy(budgets = budgets) }
            }
        }
    }

    private fun observeBudgets() {
        viewModelScope.launch {
            state
                .map { Pair(it.wallets, it.budgets) }
                .collectLatest { (wallets, budgets) ->
                    budgets.forEach { budget ->
                        launch {
                            val startOfMonth = LocalDateTime.now().startOfMonth()
                            val endOfMonth = LocalDateTime.now().endOfMonth()
                            transactionUseCases.getExpenseSum(
                                startDateRange = startOfMonth,
                                endDateRange = endOfMonth,
                                categories = setOf(budget.category),
                                wallets = wallets.toSet()
                            ).collectLatest { spent ->
                                _state.update { state ->
                                    val updatedBudgets = state.budgets.map {
                                        if (it.id == budget.id) it.copy(spentAmount = spent) else it
                                    }
                                    state.copy(budgets = updatedBudgets)
                                }
                            }
                        }
                    }
                }
        }
    }
}