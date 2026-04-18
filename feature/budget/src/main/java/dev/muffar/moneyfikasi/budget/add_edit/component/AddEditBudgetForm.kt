package dev.muffar.moneyfikasi.budget.add_edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.budget.add_edit.AddEditBudgetState
import dev.muffar.moneyfikasi.common_ui.component.text_input.CategoryInput
import dev.muffar.moneyfikasi.domain.model.Category

@Composable
fun AddEditBudgetForm(
    modifier: Modifier = Modifier,
    state: AddEditBudgetState,
    onAmountChange: (String) -> Unit,
    onCategorySelect: (Category) -> Unit,
    onAddNewCategoryClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BudgetAmountInput(
            amount = state.amount,
            error = state.amountError,
            onAmountChange = onAmountChange,
        )

        CategoryInput(
            category = state.category,
            error = state.categoryError,
            categoryOptions = state.categoryOptions,
            onCategorySelect = onCategorySelect,
            onAddNewCategoryClick = onAddNewCategoryClick
        )
    }
}
