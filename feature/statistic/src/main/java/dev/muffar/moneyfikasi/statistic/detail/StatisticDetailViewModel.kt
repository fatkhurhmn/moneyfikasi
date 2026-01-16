package dev.muffar.moneyfikasi.statistic.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
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
    private val walletUseCases: WalletUseCases,
    private val handle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticDetailState())
    val state = _state.asStateFlow()

    init {
        initState()
    }

    private fun initState() {
        val startDate = handle.get<String>(Screen.StatisticDetail.START_DATE)?.toLong() ?: return
        val endDate = handle.get<String>(Screen.StatisticDetail.END_DATE)?.toLong() ?: return
        val categoryId = handle.get<String>(Screen.StatisticDetail.CATEGORY_ID) ?: return
        viewModelScope.launch {
            val category =
                categoryUseCases.getCategoryById(UUID.fromString(categoryId)) ?: return@launch
            val wallets = walletUseCases.getAllWallets().first().toSet()
            transactionUseCases.getAllTransactions(
                startDateRange = startDate,
                endDateRange = endDate,
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