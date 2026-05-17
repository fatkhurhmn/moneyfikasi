package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonFilterChip
import dev.muffar.moneyfikasi.common_ui.component.icon.IconByName
import dev.muffar.moneyfikasi.domain.model.Category

@Composable
fun CategoryFilterChip(
    category: Category,
    isSelect: Boolean,
    onSelect: (Category) -> Unit,
) {
    CommonFilterChip(
        label = category.name,
        selected = isSelect,
        leadingIcon = {
            IconByName(
                name = category.icon,
                tint = if (isSelect) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier.size(16.dp)
            )
        },
        onSelect = { onSelect(category) },
    )
}