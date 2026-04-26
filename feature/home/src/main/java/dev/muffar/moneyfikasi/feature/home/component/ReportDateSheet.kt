package dev.muffar.moneyfikasi.feature.home.component

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
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.BottomSheetTitle
import dev.muffar.moneyfikasi.common_ui.component.button.DoubleOutlinedButton
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.utils.extension.toDateRange
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.StringExt.capitalize
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDateSheet(
    dateRange: DateRange,
    onDismissRequest: () -> Unit,
    onChoose: (DateRange) -> Unit,
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
        onChoose(it.toDateRange())
    }

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        val options = mapOf(
            TimePeriod.DAILY to stringResource(R.string.today),
            TimePeriod.WEEKLY to stringResource(R.string.this_week),
            TimePeriod.MONTHLY to stringResource(R.string.this_month)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            BottomSheetTitle(stringResource(R.string.choose_period))
            options.forEach { option ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick(option.key) }
                        .padding(horizontal = 16.dp)
                ) {
                    DateOptionItem(
                        option = option.value.capitalize(),
                        selected = option.key == dateRange.timePeriod,
                        onClick = { onClick(option.key) }
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
}

@Composable
private fun DateOptionItem(
    option: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = option.capitalize(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.W400
        )
        RadioButton(
            selected = selected,
            onClick = onClick
        )
    }
}