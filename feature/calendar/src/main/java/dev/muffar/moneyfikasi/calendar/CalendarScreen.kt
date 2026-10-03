package dev.muffar.moneyfikasi.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.calendar.component.CalendarDayCell
import dev.muffar.moneyfikasi.calendar.component.CalendarSummaryCard
import dev.muffar.moneyfikasi.calendar.component.WeekdayHeader
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.format
import org.threeten.bp.LocalDateTime

@Composable
fun CalendarScreen(
    state: CalendarState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDaySelected: (Int) -> Unit,
    onTransactionClick: (java.util.UUID, Boolean) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.title_calendar),
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                CalendarSummaryCard(
                    monthlyExpense = state.monthlyExpenseTotal,
                    monthlyIncome = state.monthlyIncomeTotal,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            item {
                val title = state.currentMonth.format("MMMM yyyy")
                dev.muffar.moneyfikasi.common_ui.component.calendar_header.CalendarHeader(
                    title = title.replaceFirstChar { it.uppercase() },
                    onPreviousClick = onPreviousMonth,
                    onNextClick = onNextMonth
                )
            }
            item {
                WeekdayHeader(modifier = Modifier.padding(horizontal = 8.dp))
            }
            item {
                CalendarGrid(
                    currentMonth = state.currentMonth,
                    dailyExpenses = state.dailyExpenses,
                    selectedDay = state.selectedDay,
                    onDaySelected = onDaySelected
                )
            }
            if (state.selectedDay != null) {
                item {
                    val day = state.selectedDay!!
                    val dateTitle = state.currentMonth.withDayOfMonth(day).format("dd MMMM yyyy")
                    Text(
                        text = "Transaksi $dateTitle",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                item {
                    if (state.selectedDayTransactions.isEmpty()) {
                        Text(
                            text = stringResource(R.string.empty_transactions_msg),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    } else {
                        // Use TransactionsList style but simplified: show list
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            state.selectedDayTransactions.forEach { tx ->
                                dev.muffar.moneyfikasi.common_ui.component.transaction.item.TransactionItem(
                                    transaction = tx,
                                    onClick = { onTransactionClick(tx.id, tx.isTransfer) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarGrid(
    currentMonth: LocalDateTime,
    dailyExpenses: Map<Int, Double>,
    selectedDay: Int?,
    onDaySelected: (Int) -> Unit
) {
    val daysInMonth = currentMonth.toLocalDate().lengthOfMonth()
    val firstDay = currentMonth.withDayOfMonth(1)
    val firstDow = firstDay.dayOfWeek.value // 1 Mon ..7 Sun
    val offset = firstDow - 1
    val today = LocalDateTime.now()
    val isCurrentMonthToday = today.year == currentMonth.year && today.monthValue == currentMonth.monthValue

    val cells = mutableListOf<Int?>()
    repeat(offset) { cells.add(null) }
    for (d in 1..daysInMonth) cells.add(d)
    while (cells.size % 7 != 0) cells.add(null)

    val rows = cells.chunked(7)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        rows.forEach { row ->
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                row.forEach { day ->
                    if (day == null) {
                        androidx.compose.foundation.layout.Box(
                            modifier = Modifier.weight(1f).padding(4.dp)
                        ) {}
                    } else {
                        val expense = dailyExpenses[day]
                        val prevExpense = if (day > 1) dailyExpenses[day - 1] else null
                        val isToday = isCurrentMonthToday && today.dayOfMonth == day
                        androidx.compose.foundation.layout.Box(modifier = Modifier.weight(1f)) {
                            CalendarDayCell(
                                day = day,
                                expense = expense,
                                isToday = isToday,
                                isSelected = selectedDay == day,
                                isCurrentMonth = true,
                                previousDayExpense = prevExpense,
                                onClick = { onDaySelected(day) }
                            )
                        }
                    }
                }
            }
        }
    }
}
