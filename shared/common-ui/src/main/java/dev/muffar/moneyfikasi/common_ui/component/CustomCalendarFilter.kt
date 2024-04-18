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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.utils.toFormattedDateTime

@Composable
fun CustomCalendarFilter(
    startDateMillis: Long,
    endDateMillis: Long,
    modifier: Modifier = Modifier,
) {
    val start = startDateMillis.toFormattedDateTime("dd/MMM/yyyy")
    val end = endDateMillis.toFormattedDateTime("dd/MMM/yyyy")

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { },
            enabled = false
        ) {
            Icon(
                imageVector = Icons.Rounded.ChevronLeft,
                contentDescription = null
            )
        }

        Text(
            text = "$start - $end",
            modifier = Modifier.padding(8.dp)
        )

        IconButton(
            onClick = { },
            enabled = false
        ) {
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
            )
        }
    }
}