package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.utils.capitalize

@Composable
fun CategoryFilterItemHeader(
    modifier: Modifier = Modifier,
    categoryType: CategoryType,
    isAllSelected: Boolean,
    onSelectAll: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelectAll() }
            .padding(horizontal = 16.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = categoryType.name.capitalize(),
            style = MaterialTheme.typography.titleMedium,
        )
        Checkbox(
            checked = isAllSelected,
            onCheckedChange = { onSelectAll() }
        )
    }
}