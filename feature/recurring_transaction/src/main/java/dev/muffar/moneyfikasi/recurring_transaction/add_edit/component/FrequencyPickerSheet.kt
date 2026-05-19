package dev.muffar.moneyfikasi.recurring_transaction.add_edit.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.BottomSheetTitle
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.utils.extensions.StringExt.capitalize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequencyPickerSheet(
    modifier: Modifier = Modifier,
    selectedFrequency: TimePeriod,
    onFrequencySelect: (TimePeriod) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val frequencies = listOf(TimePeriod.DAILY, TimePeriod.WEEKLY, TimePeriod.MONTHLY, TimePeriod.YEARLY)

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            BottomSheetTitle(title = "Select Frequency")
            frequencies.forEach { frequency ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onFrequencySelect(frequency)
                            onDismissRequest()
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = frequency == selectedFrequency,
                        onClick = null
                    )
                    Text(
                        text = frequency.name.lowercase().capitalize(),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}
