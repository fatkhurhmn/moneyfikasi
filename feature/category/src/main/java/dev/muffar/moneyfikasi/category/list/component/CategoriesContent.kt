package dev.muffar.moneyfikasi.category.list.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
            modifier = modifier,
        ) {
            items(categories, key = { it.id }) { category ->
                CategoryItem(
                    category = category,
                    onClick = {
                        onClick(category.type, category.id)
                    }
                )
            }
        }
    } else {
        EmptyDataList(
            painter = painterResource(id = R.drawable.ic_no_category),
            description = stringResource(id = R.string.no_categories)
        )
    }
}