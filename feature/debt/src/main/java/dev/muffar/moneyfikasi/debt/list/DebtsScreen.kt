package dev.muffar.moneyfikasi.debt.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonAddButton
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.common_ui.component.dialog.DebtPaymentDialog
import dev.muffar.moneyfikasi.common_ui.component.tabs.DebtTab
import dev.muffar.moneyfikasi.common_ui.component.tabs.DebtTypeTabHeader
import dev.muffar.moneyfikasi.debt.list.component.AddDebtSheet
import dev.muffar.moneyfikasi.debt.list.component.DebtsContent
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DebtsScreen(
    modifier: Modifier = Modifier,
    state: DebtsState,
    eventFlow: SharedFlow<DebtsViewModel.UiEvent>,
    onDebtClick: (UUID) -> Unit,
    onDebtPayClick: (UUID) -> Unit,
    onAddDebtClick: (DebtType) -> Unit,
    onShowAddSheet: (Boolean) -> Unit,
    onPaymentAmountChange: (String) -> Unit,
    onPaymentDismiss: () -> Unit,
    onPaymentConfirm: () -> Unit,
    onFilterChange: (DebtType?) -> Unit,
    onBackClick: () -> Unit,
) {
    val financeColors = MoneyfikasiTheme.financeColors
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
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
                onClick = { onShowAddSheet(true) }
            )
        },
        snackbarHost = { SnackbarMessage(snackbarHostState) }
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
                onClick = onDebtClick,
                onPayClick = onDebtPayClick
            )
        }
    }

    if (state.showAddSheet) {
        AddDebtSheet(
            onAddDebt = onAddDebtClick,
            onDismissRequest = { onShowAddSheet(false) }
        )
    }

    state.paymentDebt?.let { debt ->
        DebtPaymentDialog(
            debtName = debt.name,
            debtType = debt.type,
            remainingAmount = debt.remainingAmount,
            amount = state.paymentAmount,
            error = state.paymentAmountError,
            onAmountChange = onPaymentAmountChange,
            onDismiss = onPaymentDismiss,
            onConfirm = onPaymentConfirm
        )
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is DebtsViewModel.UiEvent.ShowMessage -> {
                    snackbarHostState.showMessage(
                        context.applicationContext.getString(it.messageResId),
                        it.type
                    )
                }
            }
        }
    }
}
