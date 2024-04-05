package dev.muffar.moneyfikasi.category.list.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.muffar.moneyfikasi.domain.model.Category

@Composable
fun CategoriesContent(
    modifier: Modifier = Modifier,
    categories: List<Category>,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(categories) { category ->
            CategoriesItem(
                category = category,
                onClick = {}
            )
        }
    }
}