package dev.muffar.moneyfikasi.transaction.add_edit.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.IconFieldButton
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.resource.R

@Composable
fun CategoryInput(
    category: Category,
    onCategoryClick: () -> Unit
) {
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
            onClick = onCategoryClick
        )
        Spacer(modifier = Modifier.width(16.dp))
        IconFieldButton(
            icon = category.icon,
            color = category.color,
            showLabel = false,
            onIconClick = onCategoryClick
        )
    }
}