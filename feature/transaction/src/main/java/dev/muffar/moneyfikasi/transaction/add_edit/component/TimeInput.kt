package dev.muffar.moneyfikasi.transaction.add_edit.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.TimePickerSheet
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.resource.R
import java.util.Locale

@Composable
fun TimeInput(
    modifier: Modifier = Modifier,
    time: Pair<Int, Int>,
    onTimeSelect: (Pair<Int, Int>) -> Unit
) {

    var showTimeSheet by remember { mutableStateOf(false) }
    CommonTextInput(
        modifier = modifier.fillMaxWidth(),
        value = String.format(Locale.getDefault(), "%02d:%02d", time.first, time.second),
        onValueChange = {},
        label = stringResource(R.string.time),
        placeholder = stringResource(R.string.select_time),
        isClickable = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Schedule,
                contentDescription = stringResource(R.string.select_time),
                modifier = Modifier.size(20.dp)
            )
        },
        onClick = { showTimeSheet = true }
    )

    AnimatedVisibility(showTimeSheet) {
        TimePickerSheet(
            time = time,
            onDismissRequest = { showTimeSheet = false },
            onTimeSelect = onTimeSelect
        )
    }
}