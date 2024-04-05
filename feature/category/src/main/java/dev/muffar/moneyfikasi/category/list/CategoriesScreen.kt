package dev.muffar.moneyfikasi.category.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.category.list.component.CategoriesTopBar
import dev.muffar.moneyfikasi.common_ui.component.CommonTabs
import dev.muffar.moneyfikasi.domain.model.CategoryType

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    state: CategoriesState,
) {
    Scaffold(
        topBar = {
            CategoriesTopBar(modifier = Modifier.padding(16.dp))
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
        ) {
            val tabs = CategoryType.entries.map { categoryType -> categoryType.name }
            CommonTabs(tabs = tabs) { index ->
                when (index) {
                    0 -> Text("EXPENSE 0")
                    1 -> Text("INCOME 1")
                }
            }
        }
    }
}