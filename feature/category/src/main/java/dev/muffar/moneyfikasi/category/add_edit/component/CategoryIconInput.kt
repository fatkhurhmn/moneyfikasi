package dev.muffar.moneyfikasi.category.add_edit.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.IconPickerSheet
import dev.muffar.moneyfikasi.common_ui.component.button.IconFieldButton
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.utils.CategoryIcon

@Composable
fun CategoryIconInput(
    icon: String,
    color: Long,
    onIconSelect: (String) -> Unit
) {
    var showIconPicker by remember { mutableStateOf(false) }

    IconFieldButton(
        icon = icon,
        color = color,
        onIconClick = { showIconPicker = true }
    )

    AnimatedVisibility(showIconPicker) {
        IconPickerSheet(
            icons = CategoryIcon.getCategories(CategoryType.INCOME),
            onDismissRequest = { showIconPicker = false },
            onClick = onIconSelect
        )
    }
}