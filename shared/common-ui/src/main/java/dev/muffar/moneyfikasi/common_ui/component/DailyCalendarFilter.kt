package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun DailyCalendarFilter(
    modifier: Modifier = Modifier,
    currentDate : LocalDateTime,
    onCurrentDateChange: (LocalDateTime) -> Unit,
    onDateChange: (LocalDateTime) -> Unit,
) {
    val formatter = remember { DateTimeFormatter.ofPattern("dd MMMM yyyy") }

    LaunchedEffect(currentDate) {
        onDateChange(currentDate)
    }

    Row(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clip(CircleShape)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(0.5f), CircleShape),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                val newDate = currentDate.minusDays(1)
                onCurrentDateChange(newDate)
                onDateChange(newDate)
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.ChevronLeft,
                contentDescription = stringResource(R.string.previous_day)
            )
        }

        Text(
            text = currentDate.format(formatter),
            modifier = Modifier.padding(4.dp)
        )

        IconButton(
            onClick = {
                val newDate = currentDate.plusDays(1)
                onCurrentDateChange(newDate)
                onDateChange(newDate)
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = stringResource(R.string.next_day)
            )
        }
    }
}