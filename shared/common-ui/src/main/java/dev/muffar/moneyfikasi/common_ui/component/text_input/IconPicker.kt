package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.IconPickerSheet
import dev.muffar.moneyfikasi.common_ui.component.button.IconFieldButton

@Composable
fun IconPicker(
    modifier: Modifier = Modifier,
    icon: String,
    color: Long,
    options: List<String>,
    onIconSelect: (String) -> Unit
) {
    var showIconPicker by remember { mutableStateOf(false) }

    IconFieldButton(
        modifier = modifier,
        icon = icon,
        color = color,
        onIconClick = { showIconPicker = true }
    )

    AnimatedVisibility(showIconPicker) {
        IconPickerSheet(
            icons = options,
            onDismissRequest = { showIconPicker = false },
            onClick = onIconSelect
        )
    }
}
