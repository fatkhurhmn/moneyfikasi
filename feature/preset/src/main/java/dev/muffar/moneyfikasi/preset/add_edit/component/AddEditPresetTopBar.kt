package dev.muffar.moneyfikasi.preset.add_edit.component

import androidx.compose.runtime.Composable
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.utils.extensions.capitalize

@Composable
fun AddEditPresetTopBar(
    type: TransactionType,
    onBackClick: () -> Unit
) {
    CommonTopAppBar(
        title = "Preset ${type.name.capitalize()}",
        onBackClick = onBackClick
    )
}