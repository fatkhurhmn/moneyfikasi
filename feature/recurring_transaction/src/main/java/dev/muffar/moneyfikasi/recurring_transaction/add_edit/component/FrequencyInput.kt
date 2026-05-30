package dev.muffar.moneyfikasi.recurring_transaction.add_edit.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.utils.extension.labelRes
import dev.muffar.moneyfikasi.resource.R

@Composable
fun FrequencyInput(
    modifier: Modifier = Modifier,
    frequency: TimePeriod,
    onFrequencySelect: (TimePeriod) -> Unit,
) {
    var showFrequencyPicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        CommonTextInput(
            value = stringResource(frequency.labelRes()),
            onValueChange = {},
            label = stringResource(R.string.label_frequency),
            isClickable = true,
            onClick = { showFrequencyPicker = true },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Repeat,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        )

        AnimatedVisibility(visible = showFrequencyPicker) {
            FrequencyPickerSheet(
                selectedFrequency = frequency,
                onFrequencySelect = onFrequencySelect,
                onDismissRequest = { showFrequencyPicker = false }
            )
        }
    }
}
