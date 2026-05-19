package dev.muffar.moneyfikasi.recurring_transaction.add_edit.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.StringExt.capitalize

@Composable
fun FrequencyInput(
    modifier: Modifier = Modifier,
    frequency: TimePeriod,
    onFrequencySelect: (TimePeriod) -> Unit,
) {
    var showFrequencyPicker by remember { mutableStateOf(false) }

    Column {
        CommonTextInput(
            modifier = modifier.fillMaxWidth(),
            value = frequency.name.lowercase().capitalize(),
            onValueChange = {},
            label = stringResource(R.string.frequency),
            isClickable = true,
            onClick = { showFrequencyPicker = true },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Repeat,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
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
