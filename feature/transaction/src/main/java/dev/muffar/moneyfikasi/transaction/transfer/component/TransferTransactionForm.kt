package dev.muffar.moneyfikasi.transaction.transfer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.text_input.AmountInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.BasicAmountInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.DateInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.TimeInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.WalletInput
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.transfer.TransferTransactionState

@Composable
fun TransferTransactionForm(
    modifier: Modifier = Modifier,
    state: TransferTransactionState,
    onAmountChange: (String) -> Unit,
    onSourceWalletSelect: (Wallet) -> Unit,
    onTargetWalletSelect: (Wallet) -> Unit,
    onAddNewWalletClick: () -> Unit,
    onAdminFeeChange: (String) -> Unit,
    onDateSelect: (Long) -> Unit,
    onTimeSelect: (Pair<Int, Int>) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        AmountInput(
            amount = state.amount,
            error = state.amountError,
            onAmountChange = onAmountChange
        )
        WalletInput(
            wallet = state.sourceWallet,
            error = state.sourceWalletError,
            label = stringResource(R.string.label_from),
            walletOptions = state.walletOptions,
            onWalletSelect = onSourceWalletSelect,
            onAddNewWalletClick = onAddNewWalletClick
        )
        WalletInput(
            wallet = state.targetWallet,
            error = state.targetWalletError,
            label = stringResource(R.string.label_to),
            walletOptions = state.walletOptions,
            onWalletSelect = onTargetWalletSelect,
            onAddNewWalletClick = onAddNewWalletClick
        )
        BasicAmountInput(
            amount = state.fee,
            label = stringResource(R.string.label_admin_fee),
            onAmountChange = onAdminFeeChange,
        )
        Row {
            DateInput(
                modifier = Modifier.weight(0.6f),
                date = state.date,
                onDateSelect = onDateSelect
            )
            Spacer(modifier = Modifier.width(16.dp))
            TimeInput(
                modifier = Modifier.weight(0.4f),
                time = state.hour to state.minute,
                onTimeSelect = onTimeSelect
            )
        }
    }
}
