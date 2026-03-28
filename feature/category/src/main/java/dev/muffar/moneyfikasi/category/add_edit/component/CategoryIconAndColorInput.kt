package dev.muffar.moneyfikasi.category.add_edit.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.text_input.ColorPicker
import dev.muffar.moneyfikasi.common_ui.component.text_input.IconPicker
import dev.muffar.moneyfikasi.common_ui.component.text_input.TextInputError
import dev.muffar.moneyfikasi.domain.model.AppIcon
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage

@Composable
fun CategoryIconAndColorInput(
    icon: String,
    color: Long,
    type: CategoryType,
    onIconSelect: (String) -> Unit,
    onColorSelect: (Long) -> Unit,
    error: ErrorMessage
) {
    val iconOption = if (type == CategoryType.EXPENSE) {
        AppIcon.expenseCategoryIcons
    } else {
        AppIcon.incomeCategoryIcons
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            IconPicker(
                icon = icon,
                color = color,
                options = iconOption,
                onIconSelect = onIconSelect
            )
            Spacer(modifier = Modifier.width(16.dp))
            ColorPicker(
                color = color,
                onColorSelect = onColorSelect
            )
        }
        TextInputError(error)
    }
}