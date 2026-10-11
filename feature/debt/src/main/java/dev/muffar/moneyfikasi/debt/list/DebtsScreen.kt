package dev.muffar.moneyfikasi.debt.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonAddButton
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.debt.component.DebtTab
import dev.muffar.moneyfikasi.debt.component.DebtTypeTabHeader
import dev.muffar.moneyfikasi.debt.list.component.DebtsContent
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun DebtsScreen(
    modifier: Modifier = Modifier,
    state: DebtsState,
    onDebtClick: (UUID) -> Unit,
    onAddDebtClick: (DebtType) -> Unit,
    onFilterChange: (DebtType?) -> Unit,
    onBackClick: () -> Unit,
) {
    val financeColors = MoneyfikasiTheme.financeColors
    val tabs = listOf(
        DebtTab(R.string.label_all, MaterialTheme.colorScheme.primary),
        DebtTab(R.string.label_hutang, financeColors.income),
        DebtTab(R.string.label_piutang, financeColors.expense)
    )
    val selectedTab = when (state.selectedType) {
        DebtType.DEBT -> 1
        DebtType.RECEIVABLE -> 2
        null -> 0
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.title_debts),
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            CommonAddButton(
                onClick = { onAddDebtClick(state.selectedType ?: DebtType.DEBT) }
            )
        }
    ) { paddingValues ->
        Column(modifier = modifier.padding(paddingValues)) {
            DebtTypeTabHeader(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                tabs = tabs,
                selectedTab = selectedTab,
                onTabSelected = {
                    onFilterChange(
                        when (it) {
                            1 -> DebtType.DEBT
                            2 -> DebtType.RECEIVABLE
                            else -> null
                        }
                    )
                }
            )
            DebtsContent(
                modifier = Modifier.weight(1f),
                debts = state.filteredDebts,
                wallets = state.wallets,
                onClick = onDebtClick
            )
        }
    }
}
