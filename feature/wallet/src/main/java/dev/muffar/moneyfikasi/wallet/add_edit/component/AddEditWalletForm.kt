package dev.muffar.moneyfikasi.wallet.add_edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.text_input.BasicAmountInput
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.wallet.add_edit.AddEditWalletState

@Composable
fun AddEditWalletForm(
    modifier: Modifier = Modifier,
    state: AddEditWalletState,
    onNameChange: (String) -> Unit,
    onBalanceChange: (String) -> Unit,
    onIconSelect: (String) -> Unit,
    onColorSelect: (Long) -> Unit,
    onWalletActive: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WalletNameInput(
            name = state.name,
            onNameChange = onNameChange,
            error = state.nameError
        )

        BasicAmountInput(
            amount = state.balance,
            onAmountChange = onBalanceChange,
            label = stringResource(R.string.balance),
            placeholder = stringResource(R.string.enter_wallet_balance),
            enabled = state.id == null,
        )

        WalletIconAndColorInput(
            icon = state.icon,
            color = state.color,
            onIconSelect = onIconSelect,
            onColorSelect = onColorSelect,
            error = state.iconError
        )

        if (state.id != null) {
            WalletActivationButton(
                isActive = state.isActive,
                onIsActiveChange = onWalletActive
            )
        }
    }
}