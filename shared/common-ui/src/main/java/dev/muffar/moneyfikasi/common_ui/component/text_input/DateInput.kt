package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.DatePickerSheet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LongExt.toFormattedDateTime

@Composable
fun DateInput(
    date: Long,
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.date),
    onDateSelect: (Long) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        var showDatePicker by remember { mutableStateOf(false) }
        CommonTextInput(
            value = date.toFormattedDateTime("MMM, dd yyyy"),
            onValueChange = {},
            label = label,
            isClickable = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.CalendarToday,
                    contentDescription = stringResource(R.string.select_date),
                    modifier = Modifier.size(20.dp)
                )
            },
            onClick = { showDatePicker = true }
        )

        AnimatedVisibility(showDatePicker) {
            DatePickerSheet(
                date = date,
                onDismissRequest = { showDatePicker = false },
                onDateSelect = {
                    onDateSelect(it)
                    showDatePicker = false
                }
            )
        }
    }
}