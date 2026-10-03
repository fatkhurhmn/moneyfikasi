package dev.muffar.moneyfikasi.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfMonth
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfMonth
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.toMilliseconds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarState())
    val state = _state.asStateFlow()

    init {
        loadDailyExpenses()
    }

    fun onPreviousMonth() {
        val prev = _state.value.currentMonth.minusMonths(1)
        _state.update { it.copy(currentMonth = prev.withDayOfMonth(1), selectedDay = null, selectedDayTransactions = emptyList()) }
        loadDailyExpenses()
    }

    fun onNextMonth() {
        val next = _state.value.currentMonth.plusMonths(1)
        _state.update { it.copy(currentMonth = next.withDayOfMonth(1), selectedDay = null, selectedDayTransactions = emptyList()) }
        loadDailyExpenses()
    }

    fun onDaySelected(day: Int) {
        val current = _state.value.currentMonth
        val daysInMonth = current.toLocalDate().lengthOfMonth()
        if (day !in 1..daysInMonth) return
        _state.update { it.copy(selectedDay = day) }
        loadTransactionsForDay(day)
    }

    private fun loadDailyExpenses() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val month = _state.value.currentMonth
            val start = month.startOfMonth()
            val end = month.endOfMonth()
            transactionUseCases.getAllTransactions(start, end, emptySet(), emptySet())
                .collectLatest { transactions ->
                    val expensesByDay = transactions.filter { it.isExpense }
                        .groupBy { it.date.dayOfMonth }
                        .mapValues { entry -> entry.value.sumOf { it.amount } }
                    val incomesByDay = transactions.filter { it.isIncome }
                        .groupBy { it.date.dayOfMonth }
                        .mapValues { entry -> entry.value.sumOf { it.amount } }
                    val totalExpense = expensesByDay.values.sum()
                    val totalIncome = incomesByDay.values.sum()
                    _state.update {
                        it.copy(
                            dailyExpenses = expensesByDay,
                            dailyIncomes = incomesByDay,
                            monthlyExpenseTotal = totalExpense,
                            monthlyIncomeTotal = totalIncome,
                            isLoading = false
                        )
                    }
                    // refresh selected day transactions if needed
                    _state.value.selectedDay?.let { loadTransactionsForDay(it) }
                }
        }
    }

    private fun loadTransactionsForDay(day: Int) {
        viewModelScope.launch {
            val month = _state.value.currentMonth
            val date = month.withDayOfMonth(day)
            val start = date.withHour(0).withMinute(0).withSecond(0).withNano(0).toMilliseconds()
            val end = date.withHour(23).withMinute(59).withSecond(59).withNano(999999999).toMilliseconds()
            transactionUseCases.getAllTransactions(start, end, emptySet(), emptySet())
                .collectLatest { transactions ->
                    _state.update { it.copy(selectedDayTransactions = transactions) }
                }
        }
    }
}
