package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.CommonFilterChip
import dev.muffar.moneyfikasi.common_ui.component.IconByName
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
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(16.dp)
            )
        },
        onSelect = { onSelect(category) },
    )
}