package dev.muffar.moneyfikasi.common_ui.component.bottom_sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AllInclusive
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.button.DoubleOutlinedButton
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.utils.extension.toDateRange
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LongExt.toFormattedDateTime
import dev.muffar.moneyfikasi.utils.extensions.StringExt.capitalize
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseDateSheet(
    dateRange: DateRange,
    periods: List<TimePeriod> = TimePeriod.entries,
    onDismissRequest: () -> Unit,
    onChoose: (DateRange) -> Unit,
    onCustomDateClick: () -> Unit,
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

    val onClick: (option: TimePeriod) -> Unit = {
        hideSheet()
        if (it == TimePeriod.CUSTOM) {
            onCustomDateClick()
        } else {
            onChoose(it.toDateRange())
        }
    }

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        BottomSheetTitle(stringResource(R.string.choose_date))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(periods) { option ->
                val icon = when (option) {
                    TimePeriod.DAILY -> Icons.Rounded.CalendarToday
                    TimePeriod.WEEKLY -> Icons.Rounded.DateRange
                    TimePeriod.MONTHLY -> Icons.Rounded.CalendarMonth
                    TimePeriod.YEARLY -> Icons.Rounded.Event
                    TimePeriod.ALL -> Icons.Rounded.AllInclusive
                    TimePeriod.CUSTOM -> Icons.Rounded.Tune
                }

                val showDateRate =
                    dateRange.timePeriod == TimePeriod.CUSTOM && option == TimePeriod.CUSTOM
                val start = dateRange.start.toFormattedDateTime("MMM, dd yyyy")
                val end = dateRange.end.toFormattedDateTime("MMM, dd yyyy")

                PickerOptionItem(
                    isSelected = option == dateRange.timePeriod,
                    icon = icon,
                    title = option.name.lowercase().capitalize(),
                    subtitle = if (showDateRate) "$start - $end" else null,
                    onClick = { onClick(option) }
                )
            }
        }

        CommonHorizontalDivider()
        DoubleOutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            leftText = stringResource(R.string.cancel),
            rightText = stringResource(R.string.reset),
            onLeftClick = { hideSheet() },
            onRightClick = {
                hideSheet()
                onChoose(DateRange())
            }
        )
    }
}
