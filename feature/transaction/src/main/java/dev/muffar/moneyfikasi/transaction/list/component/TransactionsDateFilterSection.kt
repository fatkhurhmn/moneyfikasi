package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.runtime.Composable
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.AllCalendarHeader
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.CustomCalendarHeader
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.DayCalendarHeader
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.MonthCalendarHeader
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.WeekCalendarHeader
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.YearCalendarHeader
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import org.threeten.bp.LocalDateTime

@Composable
fun TransactionsDateFilterSection(
    dateRange: DateRange,
    currentLocalDateTime: LocalDateTime,
    onLocalDateTimeChange: (LocalDateTime) -> Unit,
) {
    when (dateRange.timePeriod) {
        TimePeriod.DAILY -> DayCalendarHeader(
            currentDate = currentLocalDateTime,
            onCurrentDateChange = onLocalDateTimeChange,
            onDateChange = {}
        )

        TimePeriod.WEEKLY -> WeekCalendarHeader(
            currentDate = currentLocalDateTime,
            onCurrentDateChange = onLocalDateTimeChange,
            onDateChange = {}
        )

        TimePeriod.MONTHLY -> MonthCalendarHeader(
            currentDate = currentLocalDateTime,
            onCurrentDateChange = onLocalDateTimeChange,
            onDateChange = {}
        )

        TimePeriod.YEARLY -> YearCalendarHeader(
            currentDate = currentLocalDateTime,
            onCurrentDateChange = onLocalDateTimeChange,
            onDateChange = {}
        )

        TimePeriod.ALL -> {
            AllCalendarHeader(onDateChange = {})
        }

        TimePeriod.CUSTOM -> CustomCalendarHeader(
            startDateMillis = dateRange.start,
            endDateMillis = dateRange.end
        )
    }
}