package dev.muffar.moneyfikasi.budget.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.budget.BudgetItem
import dev.muffar.moneyfikasi.domain.model.Budget
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun BudgetsContent(
    modifier: Modifier = Modifier,
    budgets: List<Budget>,
    onClick: (UUID) -> Unit
) {
    if (budgets.isNotEmpty()) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(budgets, key = { it.id }) { budget ->
                BudgetItem(
                    budget = budget,
                    spentAmount = budget.spentAmount,
                    onClick = { onClick(budget.id) }
                )
            }
        }
    } else {
        EmptyDataList(
            painter = painterResource(id = R.drawable.ic_no_budget),
            title = stringResource(R.string.no_budget),
            description = stringResource(R.string.no_budget_message)
        )
    }
}
