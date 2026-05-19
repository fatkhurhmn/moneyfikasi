package dev.muffar.moneyfikasi.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.budget.BudgetUseCases
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.preset.PresetUseCases
import dev.muffar.moneyfikasi.domain.usecase.recurring_transaction.RecurringTransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val walletUseCases: WalletUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val budgetUseCases: BudgetUseCases,
    private val presetUseCases: PresetUseCases,
    private val recurringTransactionUseCases: RecurringTransactionUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(MoreState())
    val state = _state.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                walletUseCases.getAllWallets(),
                categoryUseCases.getAllCategories(),
                budgetUseCases.getAllBudgets(),
                presetUseCases.getAllPresets(),
                recurringTransactionUseCases.getAllRecurringTransactions()
            ) { wallets, categories, budgets, presets, recurringTransactions ->
                MoreState(
                    walletsCount = wallets.size,
                    activeWalletsCount = wallets.count { it.isActive },
                    categoriesCount = categories.size,
                    budgetsCount = budgets.size,
                    presetsCount = presets.size,
                    recurringTransactionsCount = recurringTransactions.size
                )
            }.collectLatest { newState ->
                _state.update { newState }
            }
        }
    }
}
