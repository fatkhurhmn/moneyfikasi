package dev.muffar.moneyfikasi.domain.model

import dev.muffar.moneyfikasi.utils.extensions.endOfMonth
import dev.muffar.moneyfikasi.utils.extensions.startOfMonth
import dev.muffar.moneyfikasi.utils.extensions.toFormattedDateTime
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

data class DateRange(
    val timePeriod: TimePeriod = TimePeriod.MONTHLY,
    val start: Long = LocalDateTime.now().with(LocalTime.MIN).startOfMonth(),
    val end: Long = LocalDateTime.now().with(LocalTime.MAX).endOfMonth()
) {
    val displayCustomRange: String
        get() = "${start.toFormattedDateTime("MMM, dd yyyy")} - ${end.toFormattedDateTime("MMM, dd yyyy")}"
}
