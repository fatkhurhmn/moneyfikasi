package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.muffar.moneyfikasi.common_ui.component.AllCalendarFilter
import dev.muffar.moneyfikasi.common_ui.component.CustomCalendarFilter
import dev.muffar.moneyfikasi.common_ui.component.DailyCalendarFilter
import dev.muffar.moneyfikasi.common_ui.component.MonthlyCalendarFilter
import dev.muffar.moneyfikasi.common_ui.component.WeeklyCalendarFilter
import dev.muffar.moneyfikasi.common_ui.component.YearlyCalendarFilter
import dev.muffar.moneyfikasi.domain.utils.TimePeriod
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
        TimePeriod.DAILY -> DailyCalendarFilter(
            currentDate = currentLocalDateTime,
            onCurrentDateChange = onLocalDateTimeChange,
            onDateChange = {
                onDateChange(it.startOfDay(), it.endOfDay())
            }
        )

        TimePeriod.WEEKLY -> WeeklyCalendarFilter(
            currentDate = currentLocalDateTime,
            onCurrentDateChange = onLocalDateTimeChange,
            onDateChange = {
                onDateChange(it.startOfWeek(), it.endOfWeek())
            }
        )

        TimePeriod.MONTHLY -> MonthlyCalendarFilter(
            currentDate = currentLocalDateTime,
            onCurrentDateChange = onLocalDateTimeChange,
            onDateChange = {
                onDateChange(it.startOfMonth(), it.endOfMonth())
            }
        )

        TimePeriod.YEARLY -> YearlyCalendarFilter(
            currentDate = currentLocalDateTime,
            onCurrentDateChange = onLocalDateTimeChange,
            onDateChange = {
                onDateChange(it.startOfYear(), it.endOfYear())
            }
        )

        TimePeriod.ALL -> {
            AllCalendarFilter(
                onDateChange = {
                    onDateChange(Long.MIN_VALUE, Long.MAX_VALUE)
                }
            )
        }

        TimePeriod.CUSTOM -> CustomCalendarFilter(
            startDateMillis = startDateMillis,
            endDateMillis = endDateMillis
        )
    }
}