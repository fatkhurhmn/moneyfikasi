package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.utils.TimePeriod
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.capitalize
import dev.muffar.moneyfikasi.utils.extensions.toFormattedDateTime
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseDateSheet(
    timePeriod: TimePeriod,
    dateRange: DateRange,
    onDismissRequest: () -> Unit,
    onChoose: (TimePeriod, DateRange) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    fun hideSheet(callback: () -> Unit) {
        scope.launch { sheetState.hide() }
        callback()
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
            Text(
                text = stringResource(R.string.choose_date),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 16.dp)
            )
            CommonHorizontalDivider()
            options.forEach { v ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { hideSheet { onChoose(v, dateRange) } }
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
                            text = v.name.capitalize(),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.W400
                        )
                        RadioButton(
                            selected = v == timePeriod,
                            onClick = { hideSheet { onChoose(v, dateRange) } }
                        )
                    }

                    AnimatedVisibility(
                        visible = v == TimePeriod.CUSTOM &&
                                timePeriod == TimePeriod.CUSTOM &&
                                dateRange.start != 0L &&
                                dateRange.end != 0L
                    ) {
                        val start = dateRange.start.toFormattedDateTime("MMM, dd yyyy")
                        val end = dateRange.end.toFormattedDateTime("MMM, dd yyyy")
                        Text(
                            text = "$start - $end",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
            }
            CommonHorizontalDivider()
            DoubleOutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                leftText = stringResource(R.string.cancel),
                rightText = stringResource(R.string.reset),
                onLeftClick = { hideSheet { onDismissRequest() } },
                onRightClick = { hideSheet { onChoose(TimePeriod.MONTHLY, DateRange(0L, 0L)) } }
            )
        }
    }
}