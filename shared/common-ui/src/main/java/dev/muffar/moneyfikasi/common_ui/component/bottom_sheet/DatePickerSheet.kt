package dev.muffar.moneyfikasi.common_ui.component.bottom_sheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.button.row.RowNegativePositiveButton
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LongExt.toFormattedDateTime
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerSheet(
    date: Long,
    selectableDates: SelectableDates = DatePickerDefaults.AllDates,
    initialDisplayedMonthMillis: Long? = date,
    onDismissRequest: () -> Unit,
    onDateSelect: (Long) -> Unit
) {

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
    ) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = date,
            initialDisplayedMonthMillis = initialDisplayedMonthMillis,
            selectableDates = selectableDates
        )
        val formattedStartDate =
            pickerState.selectedDateMillis?.toFormattedDateTime("MMM, dd yyyy")
                ?: stringResource(R.string.start_date)

        BottomSheetTitle(
            title = stringResource(R.string.select_date),
            showDivider = false
        )
        DatePicker(
            state = pickerState,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                headlineContentColor = MaterialTheme.colorScheme.onSurface,
                dividerColor = MaterialTheme.colorScheme.outline
            ),
            title = null,
            headline = {
                Text(
                    text = formattedStartDate,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        )
        CommonHorizontalDivider()
        RowNegativePositiveButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            negativeText = stringResource(R.string.cancel),
            positiveText = stringResource(R.string.select),
            onNegativeClick = {
                hideSheet()
                onDismissRequest()
            },
            onPositiveClick = {
                hideSheet()
                onDateSelect(pickerState.selectedDateMillis ?: 0)
            }
        )
    }
}