package dev.muffar.moneyfikasi.common_ui.component.calendar_header

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun YearCalendarHeader(
    currentDate : LocalDateTime,
    onCurrentDateChange: (LocalDateTime) -> Unit,
    onDateChange: (LocalDateTime) -> Unit,
) {
    val formatter = remember { DateTimeFormatter.ofPattern("yyyy") }

    CalendarHeader(
        title = currentDate.format(formatter),
        onPreviousClick = {
            val newDate = currentDate.minusYears(1)
            onCurrentDateChange(newDate)
        },
        onNextClick = {
            val newDate = currentDate.plusYears(1)
            onCurrentDateChange(newDate)
        }
    )
}