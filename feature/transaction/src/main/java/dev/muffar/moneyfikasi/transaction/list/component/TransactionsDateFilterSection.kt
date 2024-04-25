package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.muffar.moneyfikasi.common_ui.component.AllCalendarFilter
import dev.muffar.moneyfikasi.common_ui.component.CustomCalendarFilter
import dev.muffar.moneyfikasi.common_ui.component.DailyCalendarFilter
import dev.muffar.moneyfikasi.common_ui.component.MonthlyCalendarFilter
import dev.muffar.moneyfikasi.common_ui.component.WeeklyCalendarFilter
import dev.muffar.moneyfikasi.common_ui.component.YearlyCalendarFilter
import dev.muffar.moneyfikasi.domain.utils.TransactionDateFilter
import dev.muffar.moneyfikasi.utils.endOfDay
import dev.muffar.moneyfikasi.utils.endOfMonth
import dev.muffar.moneyfikasi.utils.endOfWeek
import dev.muffar.moneyfikasi.utils.endOfYear
import dev.muffar.moneyfikasi.utils.startOfDay
import dev.muffar.moneyfikasi.utils.startOfMonth
import dev.muffar.moneyfikasi.utils.startOfWeek
import dev.muffar.moneyfikasi.utils.startOfYear
import org.threeten.bp.LocalDateTime

@Composable
fun TransactionsDateFilterSection(
    modifier: Modifier = Modifier,
    filter: TransactionDateFilter,
    currentLocalDateTime:LocalDateTime,
    startDateMillis: Long,
    endDateMillis: Long,
    onLocalDateTimeChange : (LocalDateTime) -> Unit,
    onDateChange: (start: Long, end: Long) -> Unit,
) {
    when (filter) {
        TransactionDateFilter.DAILY -> DailyCalendarFilter(
            modifier = modifier,
            currentDate = currentLocalDateTime,
            onCurrentDateChange = onLocalDateTimeChange,
            onDateChange = {
                onDateChange(it.startOfDay(), it.endOfDay())
            }
        )

        TransactionDateFilter.WEEKLY -> WeeklyCalendarFilter(
            modifier = modifier,
            currentDate = currentLocalDateTime,
            onCurrentDateChange = onLocalDateTimeChange,
            onDateChange = {
                onDateChange(it.startOfWeek(), it.endOfWeek())
            }
        )

        TransactionDateFilter.MONTHLY -> MonthlyCalendarFilter(
            modifier = modifier,
            currentDate = currentLocalDateTime,
            onCurrentDateChange = onLocalDateTimeChange,
            onDateChange = {
                onDateChange(it.startOfMonth(), it.endOfMonth())
            }
        )

        TransactionDateFilter.YEARLY -> YearlyCalendarFilter(
            modifier = modifier,
            currentDate = currentLocalDateTime,
            onCurrentDateChange = onLocalDateTimeChange,
            onDateChange = {
                onDateChange(it.startOfYear(), it.endOfYear())
            }
        )

        TransactionDateFilter.ALL -> {
            AllCalendarFilter(
                modifier = modifier,
                onDateChange = {
                    onDateChange(Long.MIN_VALUE, Long.MAX_VALUE)
                }
            )
        }

        TransactionDateFilter.CUSTOM -> CustomCalendarFilter(
            modifier = modifier,
            startDateMillis = startDateMillis,
            endDateMillis = endDateMillis
        )
    }
}