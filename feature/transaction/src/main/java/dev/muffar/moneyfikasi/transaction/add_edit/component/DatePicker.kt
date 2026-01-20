package dev.muffar.moneyfikasi.transaction.add_edit.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.resource.R

@Composable
fun DatePicker(
    modifier: Modifier,
    date: String,
    onDateClick: () -> Unit
) {
    CommonTextInput(
        modifier = modifier.fillMaxWidth(),
        value = date,
        onValueChange = {},
        label = stringResource(R.string.date),
        placeholder = stringResource(R.string.select_date),
        isClickable = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.CalendarToday,
                contentDescription = stringResource(R.string.select_date),
                modifier = Modifier.size(20.dp)
            )
        },
        onClick = onDateClick
    )
}