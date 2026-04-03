package dev.muffar.moneyfikasi.preset.add_edit.component

import androidx.compose.runtime.Composable
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar

@Composable
fun AddEditPresetTopBar(
    onBackClick: () -> Unit
) {
    CommonTopAppBar(
        title = "Preset",
        onBackClick = onBackClick
    )
}