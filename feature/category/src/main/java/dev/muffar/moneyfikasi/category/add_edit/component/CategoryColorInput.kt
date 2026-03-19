package dev.muffar.moneyfikasi.category.add_edit.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.ColorPickerSheet
import dev.muffar.moneyfikasi.common_ui.component.button.ColorFieldButton

@Composable
fun CategoryColorInput(
    color: Long,
    onColorSelect: (Long) -> Unit
) {
    var showColorPicker by remember { mutableStateOf(false) }

    ColorFieldButton(
        color = color,
        onColorClick = { showColorPicker = true },
        modifier = Modifier.fillMaxWidth()
    )

    AnimatedVisibility(showColorPicker) {
        ColorPickerSheet(
            onDismissRequest = { showColorPicker = false },
            onClick = onColorSelect
        )
    }
}