package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.WalletPickerSheet
import dev.muffar.moneyfikasi.common_ui.component.button.IconFieldButton
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R

@Composable
fun WalletInput(
    wallet: Wallet,
    error: ErrorMessage = ErrorMessage(),
    label: String = stringResource(R.string.wallet),
    walletOptions: List<Wallet>,
    onWalletSelect: (Wallet) -> Unit,
    onAddNewWalletClick: () -> Unit,
    onClear: (() -> Unit)? = null
) {

    var showWalletPicker by remember { mutableStateOf(false) }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CommonTextInput(
                modifier = Modifier.weight(1f),
                value = wallet.name,
                onValueChange = {},
                onClear = onClear,
                label = label,
                placeholder = stringResource(R.string.select_wallet),
                isClickable = true,
                onClick = { showWalletPicker = true }
            )
            Spacer(modifier = Modifier.width(16.dp))
            IconFieldButton(
                icon = wallet.icon,
                color = wallet.color,
                showLabel = false,
                onIconClick = { showWalletPicker = true }
            )
        }
        TextInputError(error)
        AnimatedVisibility(showWalletPicker) {
            WalletPickerSheet(
                selectedWallet = wallet,
                walletOptions = walletOptions,
                onWalletSelect = onWalletSelect,
                onAddNewWalletClick = onAddNewWalletClick,
                onDismissRequest = { showWalletPicker = false }
            )
        }
    }
}