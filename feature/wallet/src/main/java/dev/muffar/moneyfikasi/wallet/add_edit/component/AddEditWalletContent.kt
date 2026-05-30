package dev.muffar.moneyfikasi.wallet.add_edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.ModifierExt.formModifier
import dev.muffar.moneyfikasi.wallet.add_edit.AddEditWalletState

@Composable
fun AddEditWalletContent(
    paddingValues: PaddingValues,
    state: AddEditWalletState,
    onNameChange: (String) -> Unit,
    onBalanceChange: (String) -> Unit,
    onIconSelect: (String) -> Unit,
    onColorSelect: (Long) -> Unit,
    onWalletActive: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.formModifier(paddingValues, scrollState),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        AddEditWalletForm(
            state = state,
            onNameChange = onNameChange,
            onBalanceChange = onBalanceChange,
            onIconSelect = onIconSelect,
            onColorSelect = onColorSelect,
            onWalletActive = onWalletActive,
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}