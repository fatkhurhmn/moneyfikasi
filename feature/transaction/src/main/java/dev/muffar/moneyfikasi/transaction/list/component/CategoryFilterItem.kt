package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonFilterItem
import dev.muffar.moneyfikasi.common_ui.component.IconByName
import dev.muffar.moneyfikasi.domain.model.Category

@Composable
fun CategoryFilterItem(
    category: Category,
    isSelect: Boolean,
    onSelect: (Category) -> Unit,
) {
    CommonFilterItem(
        label = category.name,
        selected = isSelect,
        leadingIcon = {
            IconByName(
                name = category.icon,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
        },
        onSelect = { onSelect(category) },
    )
}