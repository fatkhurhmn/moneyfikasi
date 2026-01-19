package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.AllCalendarHeader
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.CustomCalendarHeader
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.DayCalendarHeader
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.MonthCalendarHeader
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.WeekCalendarHeader
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.YearCalendarHeader
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.utils.extensions.endOfDay
import dev.muffar.moneyfikasi.utils.extensions.endOfMonth
import dev.muffar.moneyfikasi.utils.extensions.endOfWeek
import dev.muffar.moneyfikasi.utils.extensions.endOfYear
import dev.muffar.moneyfikasi.utils.extensions.startOfDay
import dev.muffar.moneyfikasi.utils.extensions.startOfMonth
import dev.muffar.moneyfikasi.utils.extensions.startOfWeek
import dev.muffar.moneyfikasi.utils.extensions.startOfYear
import org.threeten.bp.LocalDateTime

@Composable
fun StatisticDateFilterSection(
    modifier: Modifier = Modifier,
    filter: TimePeriod,
    currentLocalDateTime: LocalDateTime,
    startDateMillis: Long,
    endDateMillis: Long,
    onLocalDateTimeChange : (LocalDateTime) -> Unit,
    onDateChange: (start: Long, end: Long) -> Unit,
) {
    when (filter) {
        TimePeriod.DAILY -> DayCalendarHeader(
            currentDate = currentLocalDateTime,
            onCurrentDateChange = onLocalDateTimeChange,
            onDateChange = {
                onDateChange(it.startOfDay(), it.endOfDay())
            }
        )

        TimePeriod.WEEKLY -> WeekCalendarHeader(
            currentDate = currentLocalDateTime,
            onCurrentDateChange = onLocalDateTimeChange,
            onDateChange = {
                onDateChange(it.startOfWeek(), it.endOfWeek())
            }
        )

        TimePeriod.MONTHLY -> MonthCalendarHeader(
            currentDate = currentLocalDateTime,
            onCurrentDateChange = onLocalDateTimeChange,
            onDateChange = {
                onDateChange(it.startOfMonth(), it.endOfMonth())
            }
        )

        TimePeriod.YEARLY -> YearCalendarHeader(
            currentDate = currentLocalDateTime,
            onCurrentDateChange = onLocalDateTimeChange,
            onDateChange = {
                onDateChange(it.startOfYear(), it.endOfYear())
            }
        )

        TimePeriod.ALL -> {
            AllCalendarHeader(
                onDateChange = {
                    onDateChange(Long.MIN_VALUE, Long.MAX_VALUE)
                }
            )
        }

        TimePeriod.CUSTOM -> CustomCalendarHeader(
            startDateMillis = startDateMillis,
            endDateMillis = endDateMillis
        )
    }
}