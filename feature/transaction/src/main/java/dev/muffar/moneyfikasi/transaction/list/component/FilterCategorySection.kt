package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonFilterChip
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.resource.R

@Composable
fun FilterCategorySection(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    selectedCategories: Set<Category>,
    onSelectAll: (Set<Category>) -> Unit,
    onSelectAllIncome: (Set<Category>) -> Unit,
    onSelectAllExpense: (Set<Category>) -> Unit,
    onSelect: (Category) -> Unit
) {
    val incomeCategories = categories.filter { it.type == CategoryType.INCOME }
    val expenseCategories = categories.filter { it.type == CategoryType.EXPENSE }

    val allCategoriesSelected = categories.all { it in selectedCategories }
    val allIncomeSelected = incomeCategories.all { it in selectedCategories }
    val allExpenseSelected = expenseCategories.all { it in selectedCategories }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.category),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .padding(top = 8.dp)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CommonFilterChip(
                label = stringResource(R.string.all),
                selected = allCategoriesSelected,
                onSelect = {
                    val categories = if (allCategoriesSelected) emptySet() else categories.toSet()
                    onSelectAll(categories)
                }
            )

            CommonFilterChip(
                label = stringResource(R.string.income),
                selected = allIncomeSelected,
                onSelect = {
                    val categories = if (allIncomeSelected) {
                        selectedCategories - incomeCategories.toSet()
                    } else {
                        selectedCategories + incomeCategories
                    }
                    onSelectAllIncome(categories)
                }
            )

            CommonFilterChip(
                label = stringResource(R.string.expense),
                selected = allExpenseSelected,
                onSelect = {
                    val categories = if (allExpenseSelected) {
                        selectedCategories - expenseCategories.toSet()
                    } else {
                        selectedCategories + expenseCategories
                    }
                    onSelectAllExpense(categories)
                }
            )
        }

        Text(
            text = stringResource(R.string.income),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .padding(top = 8.dp)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            incomeCategories.forEach {
                CategoryFilterChip(
                    category = it,
                    isSelect = it in selectedCategories,
                    onSelect = onSelect
                )
            }
        }

        Text(
            text = stringResource(R.string.expense),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .padding(top = 8.dp)
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            expenseCategories.forEach {
                CategoryFilterChip(
                    category = it,
                    isSelect = it in selectedCategories,
                    onSelect = onSelect
                )
            }
        }
    }
}