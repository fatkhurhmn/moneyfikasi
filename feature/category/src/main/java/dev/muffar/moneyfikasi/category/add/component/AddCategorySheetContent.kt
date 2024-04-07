package dev.muffar.moneyfikasi.category.add.component

import androidx.compose.runtime.Composable
import dev.muffar.moneyfikasi.domain.model.CategoryType

@Composable
fun AddCategorySheetContent(
    type: AddCategoryBottomSheet,
    categoryType: CategoryType?,
    onIconSelect: (String) -> Unit,
    onColorSelect: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    when (type) {
        AddCategoryBottomSheet.ICON -> IconPickerSheet(
            type = categoryType ?: CategoryType.INCOME,
            onClick = onIconSelect,
            onClose = onDismiss
        )

        AddCategoryBottomSheet.COLOR -> ColorPickerSheet(
            onClick = onColorSelect,
            onClose = onDismiss
        )
    }
}

enum class AddCategoryBottomSheet {
    ICON,
    COLOR
}