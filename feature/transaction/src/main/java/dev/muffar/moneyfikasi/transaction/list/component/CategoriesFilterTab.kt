package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Category
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.resource.R

@Composable
fun CategoriesFilterTab(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    selectedCategories: Set<Category>,
    onSelectAll : (Boolean) -> Unit,
    onSelectAllSameType : (Boolean, Set<Category>) -> Unit,
    onSelect : (Category) -> Unit
) {
    val groupingCategoriesByType by remember { mutableStateOf(categories.groupBy { it.type }) }
    val isAllSelected = categories.size == selectedCategories.size

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onSelectAll(isAllSelected)
                }
                .padding(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.select_all),
                style = MaterialTheme.typography.titleMedium
            )
            Checkbox(
                checked = categories.size == selectedCategories.size,
                onCheckedChange = {
                    onSelectAll(isAllSelected)
                }
            )
        }
        if (categories.isNotEmpty()) {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                groupingCategoriesByType.keys.forEach { type ->
                    item {
                        val isAllThisTypeSelected =
                            groupingCategoriesByType[type]!!.all { it in selectedCategories }
                        val categoriesByType = groupingCategoriesByType[type]!!.toSet()

                        CategoryFilterItemHeader(
                            categoryType = type,
                            isAllSelected = isAllThisTypeSelected,
                            onSelectAll = {
                                onSelectAllSameType(isAllThisTypeSelected, categoriesByType)
                            }
                        )
                    }

                    items(groupingCategoriesByType[type]!!.size) {
                        val category = groupingCategoriesByType[type]!![it]
                        CategoryFilterItem(
                            category = category,
                            isSelect = category in selectedCategories,
                            onSelect = onSelect
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.TwoTone.Category,
                    contentDescription = stringResource(R.string.no_categories),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    modifier = Modifier.size(100.dp)
                )
                Text(text = stringResource(R.string.no_categories))
            }
        }
    }
}