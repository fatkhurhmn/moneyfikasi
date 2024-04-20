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
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun CalendarYearlyHeader(
    modifier: Modifier = Modifier,
    onDateChange : (LocalDateTime) -> Unit
) {
    var currentDate by remember { mutableStateOf(LocalDateTime.now().with(LocalTime.MIN)) }
    val formatter = remember { DateTimeFormatter.ofPattern("yyyy") }

    LaunchedEffect(currentDate) {
        onDateChange(currentDate)
    }

    Row (
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = {
                currentDate = currentDate.minusYears(1)
                onDateChange(currentDate)
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.ChevronLeft,
                contentDescription = stringResource(R.string.previous_year)
            )
        }

        Text(
            text = currentDate.format(formatter),
            modifier = Modifier.padding(8.dp)
        )

        IconButton(
            onClick = {
                currentDate = currentDate.plusYears(1)
                onDateChange(currentDate)
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = stringResource(R.string.next_year)
            )
        }
    }
}