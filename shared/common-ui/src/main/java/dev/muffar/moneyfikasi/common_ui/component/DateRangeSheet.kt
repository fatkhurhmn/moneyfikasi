package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.toFormattedDateTime
import dev.muffar.moneyfikasi.utils.toMilliseconds
import org.threeten.bp.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeSheet(
    startDateMillis: Long?,
    endDateMillis: Long?,
    onDateChange: (start: Long, end: Long) -> Unit,
    onClose: () -> Unit,
) {
    val context = LocalContext.current

    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = startDateMillis ?: LocalDateTime.now().toMilliseconds(),
        initialSelectedEndDateMillis = endDateMillis ?: LocalDateTime.now().plusDays(30)
            .toMilliseconds(),
    )
    val formattedStartDate =
        state.selectedStartDateMillis?.toFormattedDateTime("MMM, dd yyyy")
            ?: stringResource(R.string.start_date)
    val formattedEndDate =
        state.selectedEndDateMillis?.toFormattedDateTime("MMM, dd yyyy")
            ?: stringResource(R.string.end_date)

    val selectedStartDate = state.selectedStartDateMillis
    val selectedEndDate = state.selectedEndDateMillis
    var showSelectDateError by remember { mutableStateOf(false) }

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
                    text = stringResource(R.string.select_date),
                    style = MaterialTheme.typography.titleMedium
                )
                Button(
                    onClick = {
                        if (selectedStartDate == null || selectedEndDate == null) {
                            showSelectDateError = true
                        } else {
                            onDateChange(selectedStartDate, selectedEndDate)
                            onClose()
                        }
                    }
                ) {
                    Text(
                        text = stringResource(R.string.select),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        },
        headline = {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "$formattedStartDate - $formattedEndDate",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                )
                AnimatedVisibility(showSelectDateError) {
                    Text(
                        text = stringResource(R.string.please_select_date_range),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        },
        colors = DatePickerDefaults.colors(
            dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.primary.copy(0.4f),
            dayInSelectionRangeContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}