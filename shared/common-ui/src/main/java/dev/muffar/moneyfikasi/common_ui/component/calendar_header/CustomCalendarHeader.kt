package dev.muffar.moneyfikasi.common_ui.component.calendar_header

import androidx.compose.runtime.Composable
import dev.muffar.moneyfikasi.utils.extensions.toFormattedDateTime

@Composable
fun CustomCalendarHeader(
    startDateMillis: Long,
    endDateMillis: Long,
) {
    val start = startDateMillis.toFormattedDateTime("MMM, dd yyyy")
    val end = endDateMillis.toFormattedDateTime("MMM, dd yyyy")

    CalendarHeader(
        title = "$start - $end",
        enableButton = false,
    )
}