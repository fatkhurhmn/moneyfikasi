package dev.muffar.moneyfikasi.common_ui.component.calendar_header

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun DayCalendarHeader(
    currentDate: LocalDateTime,
    onCurrentDateChange: (LocalDateTime) -> Unit,
    onDateChange: (LocalDateTime) -> Unit,
) {
    val formatter = remember { DateTimeFormatter.ofPattern("dd MMMM yyyy") }

    LaunchedEffect(currentDate) {
        onDateChange(currentDate)
    }

    CalendarHeader(
        title = currentDate.format(formatter),
        onPreviousClick = {
            val newDate = currentDate.minusDays(1)
            onCurrentDateChange(newDate)
            onDateChange(newDate)
        },
        onNextClick = {
            val newDate = currentDate.plusDays(1)
            onCurrentDateChange(newDate)
            onDateChange(newDate)
        }
    )
}