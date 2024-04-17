package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.muffar.moneyfikasi.common_ui.component.CalendarAllHeader
import dev.muffar.moneyfikasi.common_ui.component.CalendarDailyHeader
import dev.muffar.moneyfikasi.common_ui.component.CalendarMonthlyHeader
import dev.muffar.moneyfikasi.common_ui.component.CalendarWeeklyHeader
import dev.muffar.moneyfikasi.common_ui.component.CalendarYearlyHeader
import dev.muffar.moneyfikasi.domain.utils.TransactionFilter
import dev.muffar.moneyfikasi.utils.endOfDay
import dev.muffar.moneyfikasi.utils.endOfMonth
import dev.muffar.moneyfikasi.utils.endOfWeek
import dev.muffar.moneyfikasi.utils.endOfYear
import dev.muffar.moneyfikasi.utils.startOfDay
import dev.muffar.moneyfikasi.utils.startOfMonth
import dev.muffar.moneyfikasi.utils.startOfWeek
import dev.muffar.moneyfikasi.utils.startOfYear

@Composable
fun TransactionsHeader(
    modifier: Modifier = Modifier,
    filter: TransactionFilter,
    onDateChange: (start: Long, end: Long) -> Unit,
) {
    when (filter) {
        TransactionFilter.ALL -> CalendarAllHeader(modifier = modifier)
        TransactionFilter.YEARLY -> CalendarYearlyHeader(
            modifier = modifier,
            onDateChange = {
                onDateChange(it.startOfYear(), it.endOfYear())
            }
        )

        TransactionFilter.MONTHLY -> CalendarMonthlyHeader(
            modifier = modifier,
            onDateChange = {
                onDateChange(it.startOfMonth(), it.endOfMonth())
            }
        )

        TransactionFilter.WEEKLY -> CalendarWeeklyHeader(
            modifier = modifier,
            onDateChange = {
                onDateChange(it.startOfWeek(), it.endOfWeek())
            }
        )

        TransactionFilter.DAILY -> CalendarDailyHeader(
            modifier = modifier,
            onDateChange = {
                onDateChange(it.startOfDay(), it.endOfDay())
            }
        )
    }
}