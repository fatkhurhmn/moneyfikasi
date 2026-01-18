package dev.muffar.moneyfikasi.domain.model

import dev.muffar.moneyfikasi.domain.utils.TimePeriod
import dev.muffar.moneyfikasi.utils.extensions.endOfMonth
import dev.muffar.moneyfikasi.utils.extensions.startOfMonth
import org.threeten.bp.LocalDateTime

data class DateRange(
    val timePeriod: TimePeriod = TimePeriod.MONTHLY,
    val start: Long = LocalDateTime.now().startOfMonth(),
    val end: Long = LocalDateTime.now().endOfMonth()
)
