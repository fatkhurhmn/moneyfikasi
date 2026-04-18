package dev.muffar.moneyfikasi.budget.list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.budget.list.component.BudgetItem
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.button.CommonAddButton
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun BudgetsScreen(
    modifier: Modifier = Modifier,
    state: BudgetsState,
    onBudgetClick: (UUID) -> Unit,
    onAddBudgetClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.budgets),
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            CommonAddButton(onClick = onAddBudgetClick)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier.padding(paddingValues)
        ) {
            items(state.budgets, key = { it.id }) { budget ->
                BudgetItem(
                    budget = budget,
                    spentAmount = 0.0, // TODO: Implement spent amount calculation
                    onClick = { onBudgetClick(budget.id) }
                )
            }
        }
    }
}
