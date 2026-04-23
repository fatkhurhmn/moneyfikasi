package dev.muffar.moneyfikasi.statistic.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.utils.extensions.endOfDay
import dev.muffar.moneyfikasi.utils.extensions.startOfDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
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

    private var category: Category? = null
    private var wallets: Set<dev.muffar.moneyfikasi.domain.model.Wallet> = emptySet()

    init {
        initState()
    }

    private fun initState() {
        val startDate = handle.get<String>(Screen.StatisticDetail.START_DATE)?.toLong() ?: return
        val endDate = handle.get<String>(Screen.StatisticDetail.END_DATE)?.toLong() ?: return
        val categoryId = handle.get<String>(Screen.StatisticDetail.CATEGORY_ID) ?: return
        viewModelScope.launch {
            category = categoryUseCases.getCategoryById(UUID.fromString(categoryId)) ?: return@launch
            wallets = walletUseCases.getAllWallets().first().toSet()

            _state.update {
                it.copy(
                    type = if (category!!.type == CategoryType.INCOME) TransactionType.INCOME else TransactionType.EXPENSE,
                )
            }

            val transactions = transactionUseCases.getAllTransactionsPaged(
                startDateRange = startDate,
                endDateRange = endDate,
                categories = setOf(category!!),
                wallets = wallets
            ).cachedIn(viewModelScope)

            _state.update { it.copy(transactions = transactions) }

            if (category!!.type == CategoryType.INCOME) {
                transactionUseCases.getIncomeSum(
                    startDateRange = startDate,
                    endDateRange = endDate,
                    categories = setOf(category!!),
                    wallets = wallets
                ).collectLatest { amount ->
                    _state.update { it.copy(totalAmount = amount) }
                }
            } else {
                transactionUseCases.getExpenseSum(
                    startDateRange = startDate,
                    endDateRange = endDate,
                    categories = setOf(category!!),
                    wallets = wallets
                ).collectLatest { amount ->
                    _state.update { it.copy(totalAmount = amount) }
                }
            }
        }
    }

    fun getDailySum(date: LocalDateTime): Flow<Double> {
        return if (category?.type == CategoryType.INCOME) {
            transactionUseCases.getIncomeSum(
                date.startOfDay(),
                date.endOfDay(),
                setOf(category!!),
                wallets
            )
        } else {
            transactionUseCases.getExpenseSum(
                date.startOfDay(),
                date.endOfDay(),
                setOf(category!!),
                wallets
            )
        }
    }
}
