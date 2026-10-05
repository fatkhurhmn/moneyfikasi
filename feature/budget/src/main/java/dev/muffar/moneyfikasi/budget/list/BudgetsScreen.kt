package dev.muffar.moneyfikasi.budget.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.budget.list.component.BudgetPeriodHeader
import dev.muffar.moneyfikasi.budget.list.component.BudgetsContent
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.BudgetCutoffPickerSheet
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonAddButton
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun BudgetsScreen(
    modifier: Modifier = Modifier,
    state: BudgetsState,
    onBudgetClick: (UUID) -> Unit,
    onAddBudgetClick: () -> Unit,
    onCutoffDayChange: (Int) -> Unit,
    onBackClick: () -> Unit,
) {
    var showPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.title_budgets),
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            CommonAddButton(onClick = onAddBudgetClick)
        }
    ) { paddingValues ->
        Column(modifier = modifier.padding(paddingValues)) {
            BudgetPeriodHeader(
                periodStart = state.periodStart,
                periodEnd = state.periodEnd,
                cutoffDay = state.budgetCutoffDay,
                onSettingsClick = { showPicker = true }
            )
            BudgetsContent(
                budgets = state.budgets,
                onClick = onBudgetClick
            )
        }

        AnimatedVisibility(showPicker) {
            BudgetCutoffPickerSheet(
                selectedDay = state.budgetCutoffDay,
                onDaySelect = onCutoffDayChange,
                onDismissRequest = { showPicker = false }
            )
        }
    }
}
