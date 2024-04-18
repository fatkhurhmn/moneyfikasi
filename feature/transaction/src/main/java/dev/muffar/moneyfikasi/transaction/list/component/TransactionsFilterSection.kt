package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.muffar.moneyfikasi.common_ui.component.AllCalendarFilter
import dev.muffar.moneyfikasi.common_ui.component.CalendarYearlyHeader
import dev.muffar.moneyfikasi.common_ui.component.CustomCalendarFilter
import dev.muffar.moneyfikasi.common_ui.component.DailyCalendarFilter
import dev.muffar.moneyfikasi.common_ui.component.MonthlyCalendarFilter
import dev.muffar.moneyfikasi.common_ui.component.WeeklyCalendarFilter
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
fun TransactionsFilterSection(
    modifier: Modifier = Modifier,
    filter: TransactionFilter,
    startDateMillis: Long,
    endDateMillis: Long,
    onDateChange: (start: Long, end: Long) -> Unit,
) {
    when (filter) {
        TransactionFilter.DAILY -> DailyCalendarFilter(
            modifier = modifier,
            onDateChange = {
                onDateChange(it.startOfDay(), it.endOfDay())
            }
        )

        TransactionFilter.WEEKLY -> WeeklyCalendarFilter(
            modifier = modifier,
            onDateChange = {
                onDateChange(it.startOfWeek(), it.endOfWeek())
            }
        )

        TransactionFilter.MONTHLY -> MonthlyCalendarFilter(
            modifier = modifier,
            onDateChange = {
                onDateChange(it.startOfMonth(), it.endOfMonth())
            }
        )

        TransactionFilter.YEARLY -> CalendarYearlyHeader(
            modifier = modifier,
            onDateChange = {
                onDateChange(it.startOfYear(), it.endOfYear())
            }
        )

        TransactionFilter.ALL -> AllCalendarFilter(modifier = modifier)

        TransactionFilter.CUSTOM -> CustomCalendarFilter(
            modifier = modifier,
            startDateMillis = startDateMillis,
            endDateMillis = endDateMillis
        )
    }
}