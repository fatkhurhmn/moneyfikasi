package dev.muffar.moneyfikasi.common_ui.component.bottom_sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.button.DoubleOutlinedButton
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.utils.extension.toDateRange
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.capitalize
import dev.muffar.moneyfikasi.utils.extensions.toFormattedDateTime
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

    val hideSheet: (callback: () -> Unit) -> Unit = {
        scope.launch { sheetState.hide() }
        it()
    }

    val onClick: (option: TimePeriod) -> Unit = {
        if (it == TimePeriod.CUSTOM) {
            hideSheet {
                onDismissRequest()
                onCustomDateClick()
            }
        } else {
            hideSheet { onChoose(it.toDateRange()) }
        }
    }

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        onDismissRequest = { hideSheet { onDismissRequest() } },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        val options = TimePeriod.entries
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            BottomSheetTitle(stringResource(R.string.choose_date))
            CommonHorizontalDivider()
            options.forEach { option ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick(option) }
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = option.name.capitalize(),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.W400
                        )
                        RadioButton(
                            selected = option == dateRange.timePeriod,
                            onClick = { onClick(option) }
                        )
                    }

                    if (dateRange.timePeriod == TimePeriod.CUSTOM && option == TimePeriod.CUSTOM) {
                        val start = dateRange.start.toFormattedDateTime("MMM, dd yyyy")
                        val end = dateRange.end.toFormattedDateTime("MMM, dd yyyy")
                        Text(
                            text = "$start - $end",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 16.dp, start = 16.dp)
                        )
                    }
                }
            }

            CommonHorizontalDivider()
            DoubleOutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                leftText = stringResource(R.string.cancel),
                rightText = stringResource(R.string.reset),
                onLeftClick = { hideSheet { onDismissRequest() } },
                onRightClick = { hideSheet { onChoose(DateRange()) } }
            )
        }
    }
}