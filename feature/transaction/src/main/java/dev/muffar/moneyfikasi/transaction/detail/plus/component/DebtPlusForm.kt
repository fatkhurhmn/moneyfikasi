package dev.muffar.moneyfikasi.transaction.detail.plus.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.tabs.DebtTab
import dev.muffar.moneyfikasi.common_ui.component.tabs.DebtTypeTabHeader
import dev.muffar.moneyfikasi.common_ui.component.text_input.BasicAmountInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.DateInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.WalletInput
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.detail.plus.DebtPlusState

@Composable
fun DebtPlusForm(
    modifier: Modifier = Modifier,
    state: DebtPlusState,
    showTabs: Boolean = true,
    onTabSelected: (Int) -> Unit,
    onAmountChange: (String) -> Unit,
    onWalletSelect: (Wallet) -> Unit,
    onAddNewWalletClick: () -> Unit,
    onDateSelect: (Long) -> Unit
) {
    val debt = state.debt ?: return
    val financeColors = MoneyfikasiTheme.financeColors
    val tabs = if (debt.type == DebtType.DEBT) {
        listOf(
            DebtTab(R.string.label_add_debt, financeColors.income),
            DebtTab(R.string.label_pay_debt, financeColors.expense)
        )
    } else {
        listOf(
            DebtTab(R.string.label_collect_debt, financeColors.income),
            DebtTab(R.string.label_add_receivable, financeColors.expense)
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (showTabs) {
            DebtTypeTabHeader(
                tabs = tabs,
                selectedTab = state.tab,
                onTabSelected = onTabSelected
            )
        }

        BasicAmountInput(
            amount = state.amount,
            error = state.amountError,
            onAmountChange = onAmountChange
        )

        WalletInput(
            wallet = state.wallet,
            error = state.walletError,
            walletOptions = state.walletOptions,
            onWalletSelect = onWalletSelect,
            onAddNewWalletClick = onAddNewWalletClick
        )

        DateInput(
            date = state.date,
            label = stringResource(R.string.label_date),
            onDateSelect = onDateSelect
        )
    }
}
