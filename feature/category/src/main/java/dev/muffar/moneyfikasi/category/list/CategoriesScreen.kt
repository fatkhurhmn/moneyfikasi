package dev.muffar.moneyfikasi.category.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.category.list.component.CategoriesContent
import dev.muffar.moneyfikasi.category.list.component.CategoriesTopBar
import dev.muffar.moneyfikasi.common_ui.component.CommonTabs
import dev.muffar.moneyfikasi.domain.model.CategoryType

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    state: CategoriesState,
) {
    val tabs = CategoryType.entries.map { categoryType -> categoryType.name }
    val expenseCategories = state.categories.filter { it.type == CategoryType.EXPENSE }
    val incomeCategories = state.categories.filter { it.type == CategoryType.INCOME }

    Scaffold(
        topBar = {
            CategoriesTopBar(modifier = Modifier.padding(16.dp))
        }
    ) {
        CommonTabs(
            modifier = modifier.padding(it),
            tabs = tabs
        ) { index ->
            when (index) {
                0 -> CategoriesContent(categories = expenseCategories)
                1 -> CategoriesContent(categories = incomeCategories)
            }
        }
    }
}