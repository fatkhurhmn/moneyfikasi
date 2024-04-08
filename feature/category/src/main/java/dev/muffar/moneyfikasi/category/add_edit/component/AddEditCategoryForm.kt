package dev.muffar.moneyfikasi.category.add_edit.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonTextInput
import dev.muffar.moneyfikasi.resource.R
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
    onIconClick: () -> Unit,
    onColorClick: () -> Unit,
    onIsActiveChange: () -> Unit,
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        CommonTextInput(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = onNameChange,
            label = stringResource(R.string.name),
            placeholder = stringResource(R.string.enter_category_name),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
        ) {
            CategoryIconButton(
                icon = icon,
                color = color,
                onIconClick = onIconClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            CategoryColorButton(
                color = color,
                onColorClick = onColorClick,
                modifier = Modifier.weight(1f)
            )
        }

        if (id != null) {
            Spacer(modifier = Modifier.height(8.dp))
            CategoryActivationButton(
                isActive = isActive,
                onIsActiveChange = onIsActiveChange
            )
        }
    }
}