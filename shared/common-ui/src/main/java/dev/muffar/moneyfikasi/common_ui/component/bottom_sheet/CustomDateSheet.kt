package dev.muffar.moneyfikasi.common_ui.component.bottom_sheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.button.RowNegativePositiveButton
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.endOfMonth
import dev.muffar.moneyfikasi.utils.extensions.startOfMonth
import dev.muffar.moneyfikasi.utils.extensions.toFormattedDateTime
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDateSheet(
    dateRange: DateRange,
    onDateChange: (DateRange) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val startDateMillis = if (dateRange.timePeriod != TimePeriod.CUSTOM) {
        LocalDateTime.now().startOfMonth()
    } else {
        dateRange.start
    }
    val endDateMillis = if (dateRange.timePeriod != TimePeriod.CUSTOM) {
        LocalDateTime.now().endOfMonth()
    } else {
        dateRange.end
    }

    var showSelectDateError by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val hideSheet = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissRequest()
            }
        }
    }

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {

        val pickerState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = startDateMillis,
            initialSelectedEndDateMillis = endDateMillis,
        )

        val formattedStartDate =
            pickerState.selectedStartDateMillis?.toFormattedDateTime("MMM, dd yyyy")
                ?: stringResource(R.string.start_date)
        val formattedEndDate =
            pickerState.selectedEndDateMillis?.toFormattedDateTime("MMM, dd yyyy")
                ?: stringResource(R.string.end_date)

        DateRangePicker(
            modifier = if (pickerState.displayMode == DisplayMode.Picker) Modifier.weight(1f) else Modifier,
            state = pickerState,
            title = {
                BottomSheetTitle(
                    title = stringResource(R.string.custom_date),
                    showDivider = false
                )
            },
            headline = {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "$formattedStartDate - $formattedEndDate",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
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
                dividerColor = MainColor.ExtraLightGray,
                dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.primary.copy(0.4f),
                dayInSelectionRangeContentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.surface,
                headlineContentColor = MaterialTheme.colorScheme.onSurface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
            )
        )
        CommonHorizontalDivider()
        RowNegativePositiveButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            negativeText = stringResource(R.string.cancel),
            positiveText = stringResource(R.string.apply),
            onNegativeClick = { hideSheet() },
            onPositiveClick = {
                val selectedStartDate = pickerState.selectedStartDateMillis
                val selectedEndDate = pickerState.selectedEndDateMillis
                if (selectedStartDate == null || selectedEndDate == null) {
                    showSelectDateError = true
                } else {
                    hideSheet()
                    onDateChange(
                        DateRange(
                            timePeriod = TimePeriod.CUSTOM,
                            start = selectedStartDate,
                            end = selectedEndDate
                        )
                    )
                }
            }
        )
    }
}