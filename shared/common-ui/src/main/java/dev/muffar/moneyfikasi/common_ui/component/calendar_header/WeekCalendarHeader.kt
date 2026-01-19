package dev.muffar.moneyfikasi.common_ui.component.calendar_header

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.muffar.moneyfikasi.utils.extensions.capitalize
import dev.muffar.moneyfikasi.utils.extensions.shortName
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.TemporalAdjusters

@Composable
fun WeekCalendarHeader(
    currentDate: LocalDateTime,
    onCurrentDateChange: (LocalDateTime) -> Unit,
    onDateChange: (LocalDateTime) -> Unit,
) {

    val startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val endOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

    val weekRangeText = remember(startOfWeek, endOfWeek) {
        formatWeekRange(startOfWeek, endOfWeek)
    }

    CalendarHeader(
        title = weekRangeText,
        onPreviousClick = {
            val newDate = currentDate.minusWeeks(1)
            onCurrentDateChange(newDate)
        },
        onNextClick = {
            val newDate = currentDate.plusWeeks(1)
            onCurrentDateChange(newDate)
        }
    )
}

private fun formatWeekRange(startOfWeek: LocalDateTime, endOfWeek: LocalDateTime): String {
    val startFormat = DateTimeFormatter.ofPattern("dd")
    val endFormat = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val sameYear = startOfWeek.year == endOfWeek.year
    val sameMonth = startOfWeek.month == endOfWeek.month

    return when {
        sameYear && sameMonth -> "${startOfWeek.format(startFormat)} - ${endOfWeek.format(endFormat)}"
        sameYear && !sameMonth -> "${startOfWeek.format(startFormat)} ${startOfWeek.month.shortName()} - " +
                "${endOfWeek.format(startFormat)} ${endOfWeek.month.shortName()} ${startOfWeek.year}"

        else -> "${startOfWeek.format(endFormat).capitalize()} - ${
            endOfWeek.format(endFormat).capitalize()
        }"
    }
}