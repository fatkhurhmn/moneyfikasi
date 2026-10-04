package dev.muffar.moneyfikasi.transaction.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TransactionFilter
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.ui.UiSettingsUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.domain.utils.extension.toDateRange
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfDay
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfMonth
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfDay
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfMonth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val walletUseCases: WalletUseCases,
    private val uiSettingsUseCases: UiSettingsUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionsState())
    val state = _state.asStateFlow()
    private var calendarJob: Job? = null
    private var calendarDayJob: Job? = null

    init {
        observeTransactions()
        loadCategories()
        loadWallets()
        observeCalendarMode()
    }

    private fun observeCalendarMode() {
        viewModelScope.launch {
            uiSettingsUseCases.getUiSettings().collectLatest { settings ->
                val persisted = settings.isTransactionCalendarMode
                if (persisted != _state.value.isCalendarMode) {
                    _state.update {
                        it.copy(
                            isCalendarMode = persisted,
                            calendarSelectedDay = null,
                            calendarSelectedDayTransactions = emptyList()
                        )
                    }
                    if (persisted) loadCalendarDailyExpenses() else {
                        calendarJob?.cancel()
                        calendarDayJob?.cancel()
                    }
                } else if (persisted && _state.value.calendarDailyBalances.isEmpty()) {
                    loadCalendarDailyExpenses()
                }
            }
        }
    }

    fun onEvent(event: TransactionsEvent) {
        when (event) {
            is TransactionsEvent.TimeReferenceChanged -> onTimeReferenceChange(event.timeReference)
            is TransactionsEvent.DateRangeChanged -> onDateRangeChange(event.dateRange)
            is TransactionsEvent.ShowFilterSheet -> onShowFilterSheet(event.show)
            is TransactionsEvent.ShowChooseDateSheet -> onShowChooseDateSheet(event.show)
            is TransactionsEvent.ShowCustomDateSheet -> onShowCustomDateSheet(event.show)
            is TransactionsEvent.ResetFilter -> onResetFilter()
            is TransactionsEvent.FilterChanged -> onFilterChange(event.filter)
            is TransactionsEvent.ToggleCalendarMode -> onToggleCalendarMode()
            is TransactionsEvent.CalendarPreviousMonth -> onCalendarPreviousMonth()
            is TransactionsEvent.CalendarNextMonth -> onCalendarNextMonth()
            is TransactionsEvent.CalendarDaySelected -> onCalendarDaySelected(event.day)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeTransactions() {
        val transactions = state
            .map { Pair(it.filter, it.dateRange) }
            .distinctUntilChanged()
            .flatMapLatest { (filter, dateRange) ->
                transactionUseCases.getAllTransactionsPaged(
                    dateRange.start,
                    dateRange.end,
                    filter.categories,
                    filter.wallets
                )
            }
            .cachedIn(viewModelScope)

        _state.update { it.copy(transactions = transactions) }
    }

    fun getDailyBalance(date: LocalDateTime): Flow<Double> {
        val filter = state.value.filter
        return transactionUseCases.getNetBalance(
            date.startOfDay(),
            date.endOfDay(),
            filter.categories,
            filter.wallets
        )
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryUseCases.getAllCategories(true)
                .collectLatest { categories ->
                    _state.update { state ->
                        state.copy(
                            categories = categories,
                            filter = state.filter.copy(categories = categories.toSet()),
                        )
                    }
                }
        }
    }


    private fun loadWallets() {
        viewModelScope.launch {
            walletUseCases.getAllWallets()
                .collectLatest { wallets ->
                    _state.update { state ->
                        state.copy(
                            wallets = wallets,
                            filter = state.filter.copy(wallets = wallets.toSet()),
                        )
                    }
                }
        }
    }

    private fun onTimeReferenceChange(dateTime: LocalDateTime) {
        val currentTimePeriod = state.value.dateRange.timePeriod
        _state.update {
            it.copy(
                timeReference = dateTime,
                dateRange = currentTimePeriod.toDateRange(dateTime)
            )
        }
    }

    private fun onDateRangeChange(dateRange: DateRange) {
        _state.update {
            it.copy(
                dateRange = dateRange,
                timeReference = LocalDateTime.now().with(LocalTime.MIN)
            )
        }
    }

    private fun onShowFilterSheet(show: Boolean) {
        _state.update { it.copy(showFilterSheet = show) }
    }

    private fun onShowChooseDateSheet(show: Boolean) {
        _state.update { it.copy(showChooseDateSheet = show) }
    }

    private fun onShowCustomDateSheet(show: Boolean) {
        _state.update { it.copy(showCustomDateSheet = show) }
    }

    private fun onResetFilter() {
        _state.update {
            it.copy(
                filter = TransactionFilter(
                    categories = it.categories.toSet(),
                    wallets = it.wallets.toSet(),
                ),
                isFilterApplied = false
            )
        }
        if (_state.value.isCalendarMode) loadCalendarDailyExpenses()
    }

    private fun onFilterChange(filter: TransactionFilter) {
        _state.update {
            val isFilterApplied =
                it.categories.size != filter.categories.size || it.wallets.size != filter.wallets.size
            it.copy(
                filter = filter,
                isFilterApplied = isFilterApplied
            )
        }
        if (_state.value.isCalendarMode) loadCalendarDailyExpenses()
    }

    private fun onToggleCalendarMode() {
        val newMode = !_state.value.isCalendarMode
        viewModelScope.launch {
            uiSettingsUseCases.setTransactionCalendarMode(newMode)
        }
    }

    private fun onCalendarPreviousMonth() {
        _state.update {
            it.copy(
                calendarMonth = it.calendarMonth.minusMonths(1).withDayOfMonth(1),
                calendarSelectedDay = null,
                calendarSelectedDayTransactions = emptyList()
            )
        }
        loadCalendarDailyExpenses()
    }

    private fun onCalendarNextMonth() {
        _state.update {
            it.copy(
                calendarMonth = it.calendarMonth.plusMonths(1).withDayOfMonth(1),
                calendarSelectedDay = null,
                calendarSelectedDayTransactions = emptyList()
            )
        }
        loadCalendarDailyExpenses()
    }

    private fun onCalendarDaySelected(day: Int) {
        val month = _state.value.calendarMonth
        val daysInMonth = month.toLocalDate().lengthOfMonth()
        if (day !in 1..daysInMonth) return
        val isSame = _state.value.calendarSelectedDay == day
        if (isSame) {
            _state.update {
                it.copy(
                    calendarSelectedDay = null,
                    calendarSelectedDayTransactions = emptyList()
                )
            }
            return
        }
        _state.update { it.copy(calendarSelectedDay = day) }
        loadCalendarDayTransactions(day)
    }

    private fun loadCalendarDayTransactions(day: Int) {
        calendarDayJob?.cancel()
        calendarDayJob = viewModelScope.launch {
            val month = _state.value.calendarMonth
            val filter = _state.value.filter
            val date = month.withDayOfMonth(day)
            val start = date.startOfDay()
            val end = date.endOfDay()
            transactionUseCases.getAllTransactions(start, end, filter.categories, filter.wallets)
                .collectLatest { transactions ->
                    _state.update { it.copy(calendarSelectedDayTransactions = transactions) }
                }
        }
    }

    private fun loadCalendarDailyExpenses() {
        calendarJob?.cancel()
        calendarJob = viewModelScope.launch {
            val month = _state.value.calendarMonth
            val filter = _state.value.filter
            val start = month.startOfMonth()
            val end = month.endOfMonth()
            transactionUseCases.getAllTransactions(start, end, filter.categories, filter.wallets)
                .collectLatest { transactions ->
                    val balancesByDay = transactions.groupBy { it.date.dayOfMonth }
                        .mapValues { entry ->
                            entry.value.sumOf { tx ->
                                when (tx.type) {
                                    TransactionType.INCOME, TransactionType.TRANSFER_IN -> tx.amount
                                    TransactionType.EXPENSE, TransactionType.TRANSFER_OUT -> -tx.amount
                                }
                            }
                        }
                    _state.update { it.copy(calendarDailyBalances = balancesByDay) }
                    _state.value.calendarSelectedDay?.let { loadCalendarDayTransactions(it) }
                }
        }
    }
}
