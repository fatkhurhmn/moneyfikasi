package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.budget.BudgetItem
import dev.muffar.moneyfikasi.domain.model.Budget
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun BudgetSection(
    modifier: Modifier = Modifier,
    budgets: List<Budget>,
    onBudgetClick: (UUID) -> Unit,
    onSeeAllBudgetsClick: () -> Unit,
    onAddBudgetClick: () -> Unit
) {
    Column(modifier = modifier) {
        DashboardLabel(
            label = stringResource(R.string.title_budgets),
            moreText = stringResource(R.string.action_see_all),
            onMoreClick = onSeeAllBudgetsClick
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (budgets.isNotEmpty()) {
            val visibleBudgets = budgets.take(3)

            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                visibleBudgets.forEachIndexed { index, budget ->

                    val shape = when {
                        visibleBudgets.size == 1 -> RoundedCornerShape(16.dp)

                        index == 0 -> RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp
                        )

                        index == visibleBudgets.lastIndex -> RoundedCornerShape(
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )

                        else -> RectangleShape
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        BudgetItem(
                            budget = budget,
                            spentAmount = budget.spentAmount,
                            showCard = false,
                        )
                    }

                    if (index < visibleBudgets.lastIndex) {
                        CommonHorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        } else {
            EmptyBudgetSection(onAddBudgetClick = onAddBudgetClick)
        }
    }
}
