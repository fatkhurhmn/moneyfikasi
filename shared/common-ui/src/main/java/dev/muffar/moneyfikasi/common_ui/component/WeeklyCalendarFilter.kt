package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.capitalize
import dev.muffar.moneyfikasi.utils.shortName
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.TemporalAdjusters

@Composable
fun WeeklyCalendarFilter(
    modifier: Modifier = Modifier,
    onDateChange : (LocalDateTime) -> Unit
) {
    var currentDate by remember { mutableStateOf(LocalDateTime.now()) }

    val startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val endOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

    val weekRangeText = remember(startOfWeek, endOfWeek) {
        formatWeekRange(startOfWeek, endOfWeek)
    }

    LaunchedEffect(currentDate) {
        onDateChange(currentDate)
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                currentDate = currentDate.minusWeeks(1)
                onDateChange(currentDate)
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.ChevronLeft,
                contentDescription = stringResource(R.string.previous_week)
            )
        }

        Text(
            text = weekRangeText,
            modifier = Modifier.padding(8.dp)
        )

        IconButton(
            onClick = {
                currentDate = currentDate.plusWeeks(1)
                onDateChange(currentDate)
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = stringResource(R.string.next_week)
            )
        }
    }
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

        else -> "${startOfWeek.format(endFormat).capitalize()} - ${endOfWeek.format(endFormat).capitalize()}"
    }
}