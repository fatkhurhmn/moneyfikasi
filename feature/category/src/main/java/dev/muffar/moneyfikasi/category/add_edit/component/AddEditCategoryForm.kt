package dev.muffar.moneyfikasi.category.add_edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.UUID

@Composable
fun AddEditCategoryForm(
    modifier: Modifier = Modifier,
    id: UUID?,
    name: String,
    icon: String,
    color: Long,
    isActive: Boolean,
    onNameChange: (String) -> Unit,
    onIconSelect: (String) -> Unit,
    onColorSelect: (Long) -> Unit,
    onIsActiveChange: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        NameInput(
            name = name,
            onNameChange = onNameChange
        )

        Row(
            modifier = Modifier
        ) {
            CategoryIconInput(
                icon = icon,
                color = color,
                onIconSelect = onIconSelect
            )
            Spacer(modifier = Modifier.width(16.dp))
            CategoryColorInput(
                color = color,
                onColorSelect = onColorSelect
            )
        }

        if (id != null) {
            CategoryActivationButton(
                isActive = isActive,
                onIsActiveChange = onIsActiveChange
            )
        }
    }
}