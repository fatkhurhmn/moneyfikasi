package dev.muffar.moneyfikasi.transaction.add_edit.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.resource.R

@Composable
fun TimeInput(
    modifier: Modifier = Modifier,
    time: String,
    onTimeClick: () -> Unit
) {
    CommonTextInput(
        modifier = modifier.fillMaxWidth(),
        value = time,
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
        onClick = onTimeClick
    )
}