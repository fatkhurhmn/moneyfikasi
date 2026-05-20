package dev.muffar.moneyfikasi.recurring_transaction.add_edit.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.DateInput
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.resource.R

@Composable
fun EndRecurringInput(
    modifier: Modifier = Modifier,
    endType: RecurringEndType,
    endDate: Long,
    occurrenceCount: String,
    onEndTypeChange: (RecurringEndType) -> Unit,
    onEndDateChange: (Long) -> Unit,
    onOccurrenceCountChange: (String) -> Unit,
) {
    var showEndTypePicker by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CommonTextInput(
                modifier = Modifier.weight(1f),
                value = when (endType) {
                    RecurringEndType.NEVER -> stringResource(R.string.never)
                    RecurringEndType.ON_DATE -> stringResource(R.string.on_date)
                    RecurringEndType.AFTER_OCCURRENCES -> stringResource(R.string.after_occurrences)
                },
                onValueChange = {},
                label = stringResource(R.string.end_recurring),
                isClickable = true,
                onClick = { showEndTypePicker = true }
            )

            if (endType == RecurringEndType.ON_DATE) {
                DateInput(
                    modifier = Modifier.weight(1.5f),
                    date = endDate,
                    onDateSelect = onEndDateChange,
                    label = stringResource(R.string.end_date)
                )
            } else if (endType == RecurringEndType.AFTER_OCCURRENCES) {
                CommonTextInput(
                    modifier = Modifier.weight(0.5f),
                    value = occurrenceCount,
                    onValueChange = onOccurrenceCountChange,
                    label = stringResource(R.string.times),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }

        AnimatedVisibility(visible = showEndTypePicker) {
            EndRecurringPickerSheet(
                selectedEndType = endType,
                onEndTypeSelect = onEndTypeChange,
                onDismissRequest = { showEndTypePicker = false }
            )
        }
    }
}
