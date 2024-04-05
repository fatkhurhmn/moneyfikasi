package dev.muffar.moneyfikasi.category.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.IconByName
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor
import dev.muffar.moneyfikasi.data.utils.ExpenseCategoryIcon
import dev.muffar.moneyfikasi.data.utils.IncomeCategoryIcon
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType

@Composable
fun CategoriesItem(
    modifier: Modifier = Modifier,
    category: Category,
    onClick: () -> Unit,
) {
    val icon = if (category.type == CategoryType.EXPENSE) {
        ExpenseCategoryIcon.fromIconName(category.icon)
    } else {
        IncomeCategoryIcon.fromIconName(category.icon)
    }

    val backgroundColor = when (icon) {
        is ExpenseCategoryIcon -> icon.iconColor
        is IncomeCategoryIcon -> icon.iconColor
        else -> 0xFF000000
    }

    Row(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(Color(backgroundColor))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                IconByName(name = category.icon, tint = MainColor.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = category.name, style = MaterialTheme.typography.bodyLarge)
        }
        Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = category.name)
    }
}