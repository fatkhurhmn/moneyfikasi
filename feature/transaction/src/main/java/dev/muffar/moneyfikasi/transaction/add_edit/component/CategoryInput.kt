package dev.muffar.moneyfikasi.transaction.add_edit.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.CategoryPickerSheet
import dev.muffar.moneyfikasi.common_ui.component.button.IconFieldButton
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.resource.R

@Composable
fun CategoryInput(
    category: Category,
    categoryOptions: List<Category>,
    onCategorySelect: (Category) -> Unit,
    onAddNewCategoryClick: () -> Unit
) {

    var showCategoryPicker by remember { mutableStateOf(false) }

    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CommonTextInput(
                modifier = Modifier.weight(1f),
                value = category.name,
                onValueChange = {},
                label = stringResource(R.string.category),
                placeholder = stringResource(R.string.select_category),
                isClickable = true,
                onClick = { showCategoryPicker = true }
            )
            Spacer(modifier = Modifier.width(16.dp))
            IconFieldButton(
                icon = category.icon,
                color = category.color,
                showLabel = false,
                onIconClick = { showCategoryPicker = true }
            )
        }

        AnimatedVisibility(showCategoryPicker) {
            CategoryPickerSheet(
                categoryOptions = categoryOptions,
                onAddNewCategoryClick = onAddNewCategoryClick,
                onCategorySelect = onCategorySelect,
                onDismissRequest = { showCategoryPicker = false }
            )
        }
    }
}