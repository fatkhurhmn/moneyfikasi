package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.calendar.CalendarDayCell
import dev.muffar.moneyfikasi.common_ui.component.calendar.WeekdayHeader
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.CalendarHeader
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.transaction.item.TransactionItem
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.format
import org.threeten.bp.LocalDateTime
import java.util.UUID

@Composable
fun TransactionCalendarSection(
    calendarMonth: LocalDateTime,
    dailyBalances: Map<Int, Double>,
    selectedDay: Int?,
    selectedDayTransactions: List<Transaction>,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDaySelected: (Int) -> Unit,
    onTransactionClick: (UUID, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        val title = calendarMonth.format("MMMM yyyy").replaceFirstChar { it.uppercase() }
        CalendarHeader(
            title = title,
            onPreviousClick = onPreviousMonth,
            onNextClick = onNextMonth
        )

        WeekdayHeader()

        PrimaryCard(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            TransactionCalendarGrid(
                calendarMonth = calendarMonth,
                dailyBalances = dailyBalances,
                selectedDay = selectedDay,
                onDaySelected = onDaySelected,
                modifier = Modifier.padding(8.dp)
            )
        }
        if (selectedDay != null) {
            val dateTitle = calendarMonth.withDayOfMonth(selectedDay).format("dd MMMM yyyy")
            Text(
                text = stringResource(R.string.label_transactions_on_date, dateTitle),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            if (selectedDayTransactions.isEmpty()) {
                Text(
                    text = stringResource(R.string.empty_transactions_msg),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectedDayTransactions.forEach { tx ->
                        TransactionItem(
                            transaction = tx,
                            onClick = { onTransactionClick(tx.id, tx.isTransfer) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionCalendarGrid(
    calendarMonth: LocalDateTime,
    dailyBalances: Map<Int, Double>,
    selectedDay: Int?,
    onDaySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val daysInMonth = calendarMonth.toLocalDate().lengthOfMonth()
    val firstDay = calendarMonth.withDayOfMonth(1)
    val firstDow = firstDay.dayOfWeek.value
    val offset = firstDow - 1
    val today = LocalDateTime.now()
    val isCurrentMonthToday =
        today.year == calendarMonth.year && today.monthValue == calendarMonth.monthValue

    val cells = mutableListOf<Int?>()
    repeat(offset) { cells.add(null) }
    for (d in 1..daysInMonth) cells.add(d)
    while (cells.size % 7 != 0) cells.add(null)

    val rows = cells.chunked(7)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                row.forEach { day ->
                    if (day == null) {
                        Box(modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)) {}
                    } else {
                        val balance = dailyBalances[day]
                        val isToday = isCurrentMonthToday && today.dayOfMonth == day
                        Box(modifier = Modifier.weight(1f)) {
                            CalendarDayCell(
                                day = day,
                                balance = balance,
                                isToday = isToday,
                                isSelected = selectedDay == day,
                                isCurrentMonth = true,
                                onClick = { onDaySelected(day) }
                            )
                        }
                    }
                }
            }
        }
    }
}
