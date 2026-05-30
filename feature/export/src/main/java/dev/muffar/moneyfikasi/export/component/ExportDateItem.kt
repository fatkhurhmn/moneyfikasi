package dev.muffar.moneyfikasi.export.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.DatePickerSheet
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingItem
import dev.muffar.moneyfikasi.utils.extensions.LongExt.formattedDate

@Composable
fun ExportDateItem(
    label: String,
    date: Long,
    onDateSelect: (Long) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    PrimaryCard(
        onClick = { showDatePicker = true }
    ) {
        SettingItem(
            title = label,
            subtitle = date.formattedDate(),
            icon = Icons.Rounded.CalendarToday,
            onClick = { showDatePicker = true }
        )
    }

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
