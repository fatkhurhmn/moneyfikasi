package dev.muffar.moneyfikasi.budget.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.budget.BudgetUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.ui.UiSettingsUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfBudgetPeriod
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfBudgetPeriod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BudgetsViewModel @Inject constructor(
    private val walletUseCases: WalletUseCases,
    private val budgetUseCases: BudgetUseCases,
    private val transactionUseCases: TransactionUseCases,
    private val uiSettingsUseCases: UiSettingsUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(BudgetsState())
    val state: StateFlow<BudgetsState> = _state.asStateFlow()

    init {
        loadWallets()
        loadBudgets()
        observeBudgets()
        observeBudgetPeriod()
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
            combine(
                state.map { it.wallets }.distinctUntilChanged(),
                state.map { it.budgets }.distinctUntilChanged(),
                uiSettingsUseCases.getUiSettings().map { it.budgetCutoffDay }.distinctUntilChanged()
            ) { wallets, budgets, cutoff ->
                Triple(wallets, budgets, cutoff)
            }.collectLatest { (wallets, budgets, cutoffDay) ->
                budgets.forEach { budget ->
                    launch {
                        val start = LocalDateTime.now().startOfBudgetPeriod(cutoffDay)
                        val end = LocalDateTime.now().endOfBudgetPeriod(cutoffDay)
                        transactionUseCases.getExpenseSum(
                            startDateRange = start,
                            endDateRange = end,
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

    private fun observeBudgetPeriod() {
        viewModelScope.launch {
            uiSettingsUseCases.getUiSettings().map { it.budgetCutoffDay }
                .distinctUntilChanged()
                .collectLatest { cutoffDay ->
                    val start = LocalDateTime.now().startOfBudgetPeriod(cutoffDay)
                    val end = LocalDateTime.now().endOfBudgetPeriod(cutoffDay)
                    _state.update { it.copy(budgetCutoffDay = cutoffDay, periodStart = start, periodEnd = end) }
                }
        }
    }

    fun setBudgetCutoffDay(day: Int) {
        viewModelScope.launch {
            uiSettingsUseCases.setBudgetCutoffDay(day)
        }
    }
}