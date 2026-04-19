package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.budget.list.component.BudgetItem
import dev.muffar.moneyfikasi.domain.model.Budget
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun BudgetSection(
    modifier: Modifier = Modifier,
    budgets: List<Budget>,
    onBudgetClick: (UUID) -> Unit,
    onSeeAllBudgetsClick: () -> Unit
) {
    if (budgets.isEmpty()) return

    Column(modifier = modifier) {
        DashboardLabel(
            label = stringResource(R.string.budgets),
            moreText = stringResource(R.string.see_all),
            onMoreClick = onSeeAllBudgetsClick
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column {
            budgets.take(3).forEach { budget ->
                BudgetItem(
                    budget = budget,
                    spentAmount = budget.spentAmount,
                    onClick = { onBudgetClick(budget.id) }
                )
            }
        }
    }
}
