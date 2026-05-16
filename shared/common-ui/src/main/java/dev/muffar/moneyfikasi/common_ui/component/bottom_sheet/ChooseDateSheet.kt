package dev.muffar.moneyfikasi.common_ui.component.bottom_sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseDateSheet(
    dateRange: DateRange,
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
        val options = TimePeriod.entries
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            BottomSheetTitle(stringResource(R.string.choose_date))

            Spacer(Modifier.height(8.dp))
            options.forEach { option ->
                val showDateRate =
                    dateRange.timePeriod == TimePeriod.CUSTOM && option == TimePeriod.CUSTOM
                val start = dateRange.start.toFormattedDateTime("MMM, dd yyyy")
                val end = dateRange.end.toFormattedDateTime("MMM, dd yyyy")

                DateOptionItem(
                    option = option,
                    selected = option == dateRange.timePeriod,
                    dateRange = if (showDateRate) "$start - $end" else null,
                    onClick = { onClick(option) }
                )
            }
            Spacer(Modifier.height(8.dp))

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
}

