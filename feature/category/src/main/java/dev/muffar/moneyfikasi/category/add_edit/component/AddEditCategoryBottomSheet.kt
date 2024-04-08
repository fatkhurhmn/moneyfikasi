package dev.muffar.moneyfikasi.category.add_edit.component

import androidx.compose.runtime.Composable
import dev.muffar.moneyfikasi.common_ui.component.ColorPicker
import dev.muffar.moneyfikasi.domain.model.CategoryType

@Composable
fun AddEditCategoryBottomSheet(
    type: AddEditCategoryBottomSheet,
    categoryType: CategoryType?,
    onIconSelect: (String) -> Unit,
    onColorSelect: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    when (type) {
        AddEditCategoryBottomSheet.ICON -> CategoryIconPicker(
            type = categoryType ?: CategoryType.INCOME,
            onClick = onIconSelect,
            onClose = onDismiss
        )

        AddEditCategoryBottomSheet.COLOR -> ColorPicker(
            onClick = onColorSelect,
            onClose = onDismiss
        )
    }
}

enum class AddEditCategoryBottomSheet {
    ICON,
    COLOR
}