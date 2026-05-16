package dev.muffar.moneyfikasi.category.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun CategoriesContent(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    onClick: (CategoryType, UUID) -> Unit
) {
    if (categories.isNotEmpty()) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 8.dp,
                bottom = 80.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories, key = { it.id }) { category ->
                CategoryItem(
                    category = category,
                    onClick = { onClick(category.type, category.id) }
                )
            }
        }
    } else {
        EmptyDataList(
            title = stringResource(id = R.string.no_categories),
            description = stringResource(id = R.string.no_categories_message)
        )
    }
}