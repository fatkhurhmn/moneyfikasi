package dev.muffar.moneyfikasi.wallet.add_edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
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
        WalletCard(
            name = state.name,
            color = state.color,
            icon = state.icon,
            balance = state.balance,
        )

        WalletNameInput(
            name = state.name,
            onNameChange = onNameChange,
            error = ErrorMessage()
        )

        WalletBalanceInput(
            id = state.id,
            balance = state.balance,
            onBalanceChange = onBalanceChange
        )

        WalletIconAndColorInput(
            icon = state.icon,
            color = state.color,
            onIconSelect = onIconSelect,
            onColorSelect = onColorSelect,
            error = ErrorMessage()
        )

        if (state.id != null) {
            Spacer(modifier = Modifier.height(16.dp))
            WalletActivationButton(
                isActive = state.isActive,
                onIsActiveChange = onWalletActive
            )
        }
    }
}