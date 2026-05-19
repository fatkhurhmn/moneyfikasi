package dev.muffar.moneyfikasi.recurring_transaction.add_edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Event
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
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.BottomSheetTitle
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.PickerOptionItem
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.StringExt.capitalize
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequencyPickerSheet(
    modifier: Modifier = Modifier,
    selectedFrequency: TimePeriod,
    onFrequencySelect: (TimePeriod) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val frequencies = listOf(TimePeriod.DAILY, TimePeriod.WEEKLY, TimePeriod.MONTHLY, TimePeriod.YEARLY)

    val hideSheet = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissRequest()
            }
        }
    }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        BottomSheetTitle(title = "Select Frequency")

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(frequencies) { frequency ->
                val icon = when (frequency) {
                    TimePeriod.DAILY -> Icons.Rounded.CalendarToday
                    TimePeriod.WEEKLY -> Icons.Rounded.DateRange
                    TimePeriod.MONTHLY -> Icons.Rounded.CalendarMonth
                    TimePeriod.YEARLY -> Icons.Rounded.Event
                    else -> Icons.Rounded.Event
                }

                PickerOptionItem(
                    isSelected = frequency == selectedFrequency,
                    icon = icon,
                    title = frequency.name.lowercase().capitalize(),
                    onClick = {
                        onFrequencySelect(frequency)
                        hideSheet()
                    }
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
                onFrequencySelect(TimePeriod.MONTHLY)
                hideSheet()
            }
        )
    }
}
