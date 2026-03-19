package dev.muffar.moneyfikasi.category.add_edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.category.add_edit.AddEditCategoryState

@Composable
fun AddEditCategoryForm(
    modifier: Modifier = Modifier,
    state: AddEditCategoryState,
    onNameChange: (String) -> Unit,
    onIconSelect: (String) -> Unit,
    onColorSelect: (Long) -> Unit,
    onCategoryActive: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        CategoryNameInput(
            name = state.name,
            onNameChange = onNameChange,
            error = state.nameError
        )

        CategoryIconAndColorInput(
            icon = state.icon,
            color = state.color,
            type = state.type,
            onIconSelect = onIconSelect,
            onColorSelect = onColorSelect,
            error = state.iconError
        )

        if (state.id != null) {
            CategoryActivationButton(
                isActive = state.isActive,
                onIsActiveChange = onCategoryActive
            )
        }
    }
}