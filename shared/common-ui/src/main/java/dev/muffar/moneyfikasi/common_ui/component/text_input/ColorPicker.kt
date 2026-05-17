package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.ColorPickerSheet
import dev.muffar.moneyfikasi.common_ui.component.button.ColorFieldButton

@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    color: Long,
    onColorSelect: (Long) -> Unit
) {
    var showColorPicker by remember { mutableStateOf(false) }

    ColorFieldButton(
        modifier = modifier,
        color = color,
        onColorClick = { showColorPicker = true }
    )

    AnimatedVisibility(showColorPicker) {
        ColorPickerSheet(
            onDismissRequest = { showColorPicker = false },
            onClick = onColorSelect
        )
    }
}
