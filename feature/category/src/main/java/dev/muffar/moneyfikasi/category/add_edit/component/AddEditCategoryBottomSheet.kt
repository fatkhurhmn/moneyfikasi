package dev.muffar.moneyfikasi.category.add_edit.component

import androidx.compose.runtime.Composable
import dev.muffar.moneyfikasi.common_ui.component.ColorPicker
import dev.muffar.moneyfikasi.common_ui.component.IconPicker
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.utils.CategoryIcon

@Composable
fun AddEditCategoryBottomSheet(
    type: AddEditCategoryBottomSheet,
    categoryType: CategoryType?,
    onIconSelect: (String) -> Unit,
    onColorSelect: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val icons = CategoryIcon.getCategories(categoryType?: CategoryType.INCOME)
    when (type) {
        AddEditCategoryBottomSheet.ICON -> IconPicker(
            icons = icons,
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