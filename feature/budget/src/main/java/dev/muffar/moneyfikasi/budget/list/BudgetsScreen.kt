package dev.muffar.moneyfikasi.budget.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.budget.list.component.BudgetsContent
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
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
        BudgetsContent(
            modifier = modifier.padding(paddingValues),
            budgets = state.budgets,
            onClick = onBudgetClick
        )
    }
}
