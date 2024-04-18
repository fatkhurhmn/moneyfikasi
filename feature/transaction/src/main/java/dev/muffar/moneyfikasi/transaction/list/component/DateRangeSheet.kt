package dev.muffar.moneyfikasi.transaction.list.component

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.utils.toFormattedDateTime
import dev.muffar.moneyfikasi.utils.toMilliseconds
import org.threeten.bp.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeSheet(
    startDateMillis : Long?,
    endDateMillis : Long?,
    onDateChange: (start: Long, end: Long) -> Unit,
    onClose: () -> Unit,
) {
    val context = LocalContext.current

    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = startDateMillis ?: LocalDateTime.now().toMilliseconds(),
        initialSelectedEndDateMillis = endDateMillis ?: LocalDateTime.now().plusDays(30).toMilliseconds(),
        initialDisplayMode = DisplayMode.Input
    )
    val formattedStartDate =
        state.selectedStartDateMillis?.toFormattedDateTime("MM/dd/yyyy") ?: "Start Date"
    val formattedEndDate =
        state.selectedEndDateMillis?.toFormattedDateTime("MM/dd/yyyy") ?: "End Date"

    val selectedStartDate = state.selectedStartDateMillis
    val selectedEndDate = state.selectedEndDateMillis


    DateRangePicker(
        modifier = Modifier.fillMaxSize(),
        state = state,
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Select Date Range",
                    style = MaterialTheme.typography.titleMedium
                )
                TextButton(
                    onClick = {
                        if (selectedStartDate == null || selectedEndDate == null) {
                            Toast.makeText(
                                context,
                                "Please select start date & end date",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            onDateChange(selectedStartDate, selectedEndDate)
                            onClose()
                        }
                    }
                ) {
                    Text(text = "Select", style = MaterialTheme.typography.titleMedium)
                }
            }
        },
        headline = {
            Text(
                text = "$formattedStartDate - $formattedEndDate",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
    )
}