package dev.muffar.moneyfikasi.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.TrendResult
import dev.muffar.moneyfikasi.domain.model.TrendType
import dev.muffar.moneyfikasi.domain.usecase.budget.BudgetUseCases
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.ui.UiSettingsUseCases
import dev.muffar.moneyfikasi.domain.usecase.preset.PresetUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfDay
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfMonth
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfWeek
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfYear
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfDay
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfMonth
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfWeek
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfYear
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
import kotlin.math.abs

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val walletUseCases: WalletUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val transactionUseCases: TransactionUseCases,
    private val uiSettingsUseCases: UiSettingsUseCases,
    private val presetUseCases: PresetUseCases,
    private val budgetUseCases: BudgetUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        getPreferences()
        loadCategories()
        loadWallets()
        observeTransactions()
        loadRecentTransactions()
        loadPresets()
        loadBudgets()
        observeBudgets()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Refresh -> {}
            is HomeEvent.ToggleBalanceVisibility -> toggleBalanceVisibility()
            is HomeEvent.ToggleReportVisibility -> toggleReportVisibility()
            is HomeEvent.DateRangeChanged -> onDateRangeChange(event.dateRange)
            is HomeEvent.ShowReportDateSheet -> onShowReportDateSheet(event.show)
            is HomeEvent.ShowCustomDateSheet -> onShowCustomDateSheet(event.show)
            is HomeEvent.ShowDashboardSettingsSheet -> onShowDashboardSettingsSheet(event.show)
            is HomeEvent.QuickTransactionVisibilityChanged -> onQuickTransactionVisibilityChange(event.isVisible)
            is HomeEvent.BudgetVisibilityChanged -> onBudgetVisibilityChange(event.isVisible)
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
                        transactionUseCases.getIncomeSum(
                            dateRange.start,
                            dateRange.end,
                            categories,
                            wallets
                        ),
                        transactionUseCases.getExpenseSum(
                            dateRange.start,
                            dateRange.end,
                            categories,
                            wallets
                        ),
                        transactionUseCases.getNetBalance(
                            dateRange.start,
                            dateRange.end,
                            categories,
                            wallets
                        ),
                        transactionUseCases.getNetBalance(
                            previousRange.first,
                            previousRange.second,
                            categories,
                            wallets
                        ),
                    ) { currentIncome, currentExpense, currentBalance, prevBalance ->
                        _state.update { state ->
                            state.copy(
                                reportIncome = currentIncome,
                                reportExpense = currentExpense,
                                reportNet = currentBalance,
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
                    _state.update { it.copy(recentTransactions = transactions) }
                }
        }
    }

    private fun loadPresets() {
        viewModelScope.launch {
            presetUseCases.getAllPresets()
                .collectLatest { presets ->
                    _state.update { it.copy(presets = presets) }
                }
        }
    }

    private fun loadBudgets() {
        val startOfMonth = LocalDateTime.now().startOfMonth()

        val endOfMonth = LocalDateTime.now().endOfMonth()

        viewModelScope.launch {
            budgetUseCases.getAllBudgets().collectLatest { budgets ->
                _state.update { it.copy(budgets = budgets) }
                budgets.forEach { budget ->
                    launch {
                        transactionUseCases.getExpenseSum(
                            startDateRange = startOfMonth,
                            endDateRange = endOfMonth,
                            categories = setOf(budget.category),
                            wallets = emptySet()
                        ).collectLatest { spent ->
                            _state.update { state ->
                                val updatedBudgets = state.budgets.map {
                                    if (it.id == budget.id) it.copy(spentAmount = spent) else it
                                }
                                    .sortedByDescending { if (it.amount > 0) it.spentAmount / it.amount else 0.0 }
                                state.copy(budgets = updatedBudgets)
                            }
                        }
                    }
                }
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

    fun calculateTrend(current: Double, previous: Double): TrendResult {

        if (current == 0.0 && previous == 0.0) {
            return TrendResult(
                percentage = 0.0,
                type = TrendType.NEUTRAL
            )
        }

        if (previous == 0.0) {
            return if (current > 0) {
                TrendResult(
                    percentage = 0.0,
                    type = TrendType.NEW_GROWTH
                )
            } else {
                TrendResult(
                    percentage = 0.0,
                    type = TrendType.NEW_LOSS
                )
            }
        }

        val change = current - previous
        val percentage = (change / abs(previous)) * 100

        return when {
            percentage > 0 -> {
                TrendResult(
                    percentage = percentage,
                    type = TrendType.UP
                )
            }

            percentage < 0 -> {
                TrendResult(
                    percentage = percentage,
                    type = TrendType.DOWN
                )
            }

            else -> {
                TrendResult(
                    percentage = 0.0,
                    type = TrendType.NEUTRAL
                )
            }
        }
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
        uiSettingsUseCases.getUiSettings()
            .onEach { settings ->
                _state.update {
                    it.copy(
                        isBalanceVisible = settings.isBalanceVisible,
                        isReportVisible = settings.isReportVisible,
                        isQuickTransactionVisible = settings.isQuickTransactionVisible,
                        isBudgetVisible = settings.isBudgetVisible
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun toggleBalanceVisibility() {
        viewModelScope.launch {
            uiSettingsUseCases.setBalanceVisibility(!_state.value.isBalanceVisible)
        }
    }

    private fun toggleReportVisibility() {
        viewModelScope.launch {
            uiSettingsUseCases.setReportVisibility(!_state.value.isReportVisible)
        }
    }

    private fun onShowReportDateSheet(show: Boolean) {
        _state.update { it.copy(showReportDateSheet = show) }
    }

    private fun onShowCustomDateSheet(show: Boolean) {
        _state.update { it.copy(showCustomDateSheet = show) }
    }

    private fun onShowDashboardSettingsSheet(show: Boolean) {
        _state.update { it.copy(showDashboardSettingsSheet = show) }
    }

    private fun onQuickTransactionVisibilityChange(isVisible: Boolean) {
        viewModelScope.launch {
            uiSettingsUseCases.setQuickTransactionVisibility(isVisible)
        }
    }

    private fun onBudgetVisibilityChange(isVisible: Boolean) {
        viewModelScope.launch {
            uiSettingsUseCases.setBudgetVisibility(isVisible)
        }
    }

    private fun onDateRangeChange(dateRange: DateRange) {
        _state.update { it.copy(dateRange = dateRange) }
    }
}
