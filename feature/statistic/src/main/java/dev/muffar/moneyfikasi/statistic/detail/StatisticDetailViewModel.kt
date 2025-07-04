package dev.muffar.moneyfikasi.statistic.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class StatisticDetailViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val walletUseCases: WalletUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticDetailState())
    val state = _state.asStateFlow()

    fun onEvent(event: StatisticDetailEvent) {
        when (event) {
            is StatisticDetailEvent.OnInitState -> initState(event.dateRange, event.categoryId)
        }
    }

    private fun initState(dateRange: Pair<Long, Long>, categoryId: UUID) {
        viewModelScope.launch {
            val category = categoryUseCases.getCategoryById(categoryId) ?: return@launch
            val wallets = walletUseCases.getAllWallets().first().toSet()
            transactionUseCases.getAllTransactions(
                startDateRange = dateRange.first,
                endDateRange = dateRange.second,
                categories = setOf(category),
                wallets = wallets
            ).collectLatest { transactions ->
                _state.update {
                    it.copy(
                        transactions = transactions,
                        type = transactions.firstOrNull()?.type ?: TransactionType.EXPENSE
                    )
                }
            }
        }
    }
}