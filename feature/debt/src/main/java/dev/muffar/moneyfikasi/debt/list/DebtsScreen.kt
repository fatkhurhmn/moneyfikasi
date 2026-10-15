package dev.muffar.moneyfikasi.debt.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
    onPaymentAmountChange: (String) -> Unit,
    onPaymentDismiss: () -> Unit,
    onPaymentConfirm: () -> Unit,
    onStatusFilterChange: (DebtStatusFilter) -> Unit,
    onBackClick: () -> Unit,
) {
    val financeColors = MoneyfikasiTheme.financeColors
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
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
        },
        floatingActionButton = {
            CommonAddButton(
                onClick = { onAddDebtClick(state.selectedType ?: DebtType.DEBT) }
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
                wallets = state.wallets,
                onClick = onDebtClick,
                onPayClick = onDebtPayClick
            )
        }
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
