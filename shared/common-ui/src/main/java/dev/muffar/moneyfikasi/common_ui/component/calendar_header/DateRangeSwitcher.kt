package dev.muffar.moneyfikasi.common_ui.component.calendar_header

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.shortName
import dev.muffar.moneyfikasi.utils.extensions.StringExt.capitalize
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.TemporalAdjusters

@Composable
fun DateRangeSwitcher(
    timeReference: LocalDateTime,
    dateRange: DateRange,
    onTimeReferenceChange: (LocalDateTime) -> Unit,
) {
    val formattedTitle: (format: String) -> String = {
        timeReference.format(DateTimeFormatter.ofPattern(it))
    }

    when (dateRange.timePeriod) {
        TimePeriod.DAILY -> CalendarHeader(
            title = formattedTitle("dd MMM yyyy"),
            onPreviousClick = { onTimeReferenceChange(timeReference.minusDays(1)) },
            onNextClick = { onTimeReferenceChange(timeReference.plusDays(1)) },
        )

        TimePeriod.WEEKLY -> CalendarHeader(
            title = formatWeekRange(timeReference),
            onPreviousClick = { onTimeReferenceChange(timeReference.minusWeeks(1)) },
            onNextClick = { onTimeReferenceChange(timeReference.plusWeeks(1)) },
        )

        TimePeriod.MONTHLY -> CalendarHeader(
            title = formattedTitle("MMMM yyyy"),
            onPreviousClick = { onTimeReferenceChange(timeReference.minusMonths(1)) },
            onNextClick = { onTimeReferenceChange(timeReference.plusMonths(1)) },
        )

        TimePeriod.YEARLY -> CalendarHeader(
            title = formattedTitle("yyyy"),
            onPreviousClick = { onTimeReferenceChange(timeReference.minusYears(1)) },
            onNextClick = { onTimeReferenceChange(timeReference.plusYears(1)) },
        )

        TimePeriod.ALL -> CalendarHeader(
            title = stringResource(R.string.label_all),
            enableButton = false,
        )

        TimePeriod.CUSTOM -> CalendarHeader(
            title = dateRange.displayCustomRange,
            enableButton = false,
        )
    }
}

private fun formatWeekRange(dateTime: LocalDateTime): String {
    val startOfWeek = dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val endOfWeek = dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

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