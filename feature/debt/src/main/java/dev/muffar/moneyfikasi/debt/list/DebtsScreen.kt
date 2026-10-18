package dev.muffar.moneyfikasi.debt.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.common_ui.component.tabs.DebtTab
import dev.muffar.moneyfikasi.common_ui.component.tabs.DebtTypeTabHeader
import dev.muffar.moneyfikasi.debt.list.component.DebtsContent
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun DebtsScreen(
    modifier: Modifier = Modifier,
    state: DebtsState,
    onDebtClick: (UUID) -> Unit,
    onStatusFilterChange: (DebtStatusFilter) -> Unit,
    onBackClick: () -> Unit,
) {
    val financeColors = MoneyfikasiTheme.financeColors
    val tabs = listOf(
        DebtTab(R.string.label_unpaid, financeColors.expense),
        DebtTab(R.string.label_paid_off, financeColors.income)
    )
    val selectedTab = when (state.statusFilter) {
        DebtStatusFilter.UNPAID -> 0
        DebtStatusFilter.PAID -> 1
    }
    val titleRes = if (state.selectedType == DebtType.RECEIVABLE) {
        R.string.label_piutang
    } else {
        R.string.label_hutang
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(titleRes),
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(modifier = modifier.padding(paddingValues)) {
            DebtTypeTabHeader(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                tabs = tabs,
                selectedTab = selectedTab,
                onTabSelected = {
                    onStatusFilterChange(
                        when (it) {
                            1 -> DebtStatusFilter.PAID
                            else -> DebtStatusFilter.UNPAID
                        }
                    )
                }
            )
            DebtsContent(
                modifier = Modifier.weight(1f),
                debts = state.filteredDebts,
                onClick = onDebtClick
            )
        }
    }
}
