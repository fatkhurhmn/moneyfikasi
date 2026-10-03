package dev.muffar.moneyfikasi.calendar

import dev.muffar.moneyfikasi.domain.model.Transaction
import org.threeten.bp.LocalDateTime

data class CalendarState(
    val currentMonth: LocalDateTime = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0),
    val dailyExpenses: Map<Int, Double> = emptyMap(),
    val dailyIncomes: Map<Int, Double> = emptyMap(),
    val monthlyExpenseTotal: Double = 0.0,
    val monthlyIncomeTotal: Double = 0.0,
    val selectedDay: Int? = null,
    val selectedDayTransactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false
)
