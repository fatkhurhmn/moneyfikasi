package dev.muffar.moneyfikasi.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.PreferencesUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.utils.extensions.endOfDay
import dev.muffar.moneyfikasi.utils.extensions.endOfMonth
import dev.muffar.moneyfikasi.utils.extensions.endOfWeek
import dev.muffar.moneyfikasi.utils.extensions.endOfYear
import dev.muffar.moneyfikasi.utils.extensions.startOfDay
import dev.muffar.moneyfikasi.utils.extensions.startOfMonth
import dev.muffar.moneyfikasi.utils.extensions.startOfWeek
import dev.muffar.moneyfikasi.utils.extensions.startOfYear
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val walletUseCases: WalletUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val transactionUseCases: TransactionUseCases,
    private val preferencesUseCases: PreferencesUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        getPreferences()
        loadCategories()
        loadWallets()
        observeTransactions()
        loadRecentTransactions()
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.Refresh -> {}
            is DashboardEvent.ToggleBalanceVisibility -> toggleBalanceVisibility()
            is DashboardEvent.DateRangeChanged -> onDateRangeChange(event.dateRange)
            is DashboardEvent.ShowReportDateSheet -> onShowReportDateSheet(event.show)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeTransactions() {
        viewModelScope.launch {
            state
                .map { Triple(it.dateRange, it.categories, it.wallets) }
                .distinctUntilChanged()
                .collectLatest { (dateRange, categories, wallets) ->
                    val previousRange = getPreviousDateRange(dateRange)
                    combine(
                        transactionUseCases.getIncomeSum(dateRange.start, dateRange.end, categories, wallets),
                        transactionUseCases.getExpenseSum(dateRange.start, dateRange.end, categories, wallets),
                        transactionUseCases.getNetBalance(dateRange.start, dateRange.end, categories, wallets),
                        transactionUseCases.getNetBalance(previousRange.first, previousRange.second, categories, wallets),
                    ) { currentIncome, currentExpense, currentBalance, prevBalance ->
                        _state.update { state ->
                            state.copy(
                                reportIncome = currentIncome,
                                reportExpense = currentExpense,
                                reportBalance = currentBalance,
                                balanceTrend = calculateTrend(currentBalance, prevBalance)
                            )
                        }
                    }.collectLatest { }
                }
        }
    }

    private fun loadRecentTransactions() {
        viewModelScope.launch {
            transactionUseCases.getRecentTransactions(5)
                .collectLatest { transactions ->
                    _state.update { it.copy(lastTransactions = transactions) }
                }
        }
    }

    private fun getPreviousDateRange(dateRange: DateRange): Pair<Long, Long> {
        val startDateTime: LocalDateTime = LocalDateTime.now().with(LocalTime.MIN)
        return when (dateRange.timePeriod) {
            TimePeriod.DAILY -> {
                val prev = startDateTime.minusDays(1)
                prev.startOfDay() to prev.endOfDay()
            }

            TimePeriod.WEEKLY -> {
                val prev = startDateTime.minusWeeks(1)
                prev.startOfWeek() to prev.endOfWeek()
            }

            TimePeriod.MONTHLY -> {
                val prev = startDateTime.minusMonths(1)
                prev.startOfMonth() to prev.endOfMonth()
            }

            TimePeriod.YEARLY -> {
                val prev = startDateTime.minusYears(1)
                prev.startOfYear() to prev.endOfYear()
            }

            else -> 0L to 0L
        }
    }

    private fun calculateTrend(current: Double, previous: Double): Double {
        if (previous == 0.0) return if (current > 0) 100.0 else 0.0
        return ((current - previous) / previous) * 100
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryUseCases.getAllCategories(true)
                .collectLatest { categories ->
                    _state.update {
                        it.copy(categories = categories.toSet())
                    }
                }
        }
    }

    private fun loadWallets() {
        viewModelScope.launch {
            walletUseCases.getAllWallets()
                .collectLatest { wallets ->
                    val totalBalance = wallets.sumOf { it.balance }
                    _state.update { state ->
                        state.copy(
                            totalBalance = totalBalance,
                            wallets = wallets.toSet()
                        )
                    }
                }
        }
    }

    private fun getPreferences() {
        preferencesUseCases.isBalanceVisible()
            .onEach { isVisible ->
                _state.update { it.copy(isBalanceVisible = isVisible) }
            }.launchIn(viewModelScope)
    }

    private fun toggleBalanceVisibility() {
        viewModelScope.launch {
            preferencesUseCases.setBalanceVisibility(!_state.value.isBalanceVisible)
        }
    }

    private fun onShowReportDateSheet(show: Boolean) {
        _state.update { it.copy(showReportDateSheet = show) }
    }

    private fun onDateRangeChange(dateRange: DateRange) {
        _state.update { it.copy(dateRange = dateRange) }
    }
}
