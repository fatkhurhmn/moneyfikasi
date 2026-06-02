package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.CategoryPickerSheet
import dev.muffar.moneyfikasi.common_ui.component.icon.BoxedIcon
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.constants.UUIDConst

@Composable
fun CategoryInput(
    category: Category,
    error: ErrorMessage = ErrorMessage(),
    categoryOptions: List<Category>,
    onCategorySelect: (Category) -> Unit,
    onAddNewCategoryClick: () -> Unit,
    onClear: (() -> Unit)? = null
) {

    var showCategoryPicker by remember { mutableStateOf(false) }

    Column {
        CommonTextInput(
            modifier = Modifier.fillMaxWidth(),
            value = category.name,
            onValueChange = {},
            onClear = onClear,
            label = stringResource(R.string.label_category),
            isClickable = true,
            error = error,
            onClick = { showCategoryPicker = true },
            leadingIcon = {
                val iconColor = if (category.id == UUIDConst.empty) {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(0.7f).toArgb().toLong()
                } else {
                    category.color
                }
                BoxedIcon(
                    icon = category.icon,
                    color = iconColor
                )
            }
        )
        AnimatedVisibility(showCategoryPicker) {
            CategoryPickerSheet(
                selectedCategory = category,
                categoryOptions = categoryOptions,
                onAddNewCategoryClick = onAddNewCategoryClick,
                onCategorySelect = onCategorySelect,
                onDismissRequest = { showCategoryPicker = false }
            )
        }
    }
}
