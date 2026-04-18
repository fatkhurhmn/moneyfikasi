package dev.muffar.moneyfikasi.budget.add_edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.budget.add_edit.AddEditBudgetState
import dev.muffar.moneyfikasi.common_ui.component.text_input.CategoryInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AddEditBudgetForm(
    modifier: Modifier = Modifier,
    state: AddEditBudgetState,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategorySelect: (Category) -> Unit,
    onAddNewCategoryClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CommonTextInput(
            value = state.name,
            onValueChange = onNameChange,
            label = stringResource(R.string.name),
            placeholder = stringResource(R.string.budget_name_placeholder)
        )

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
