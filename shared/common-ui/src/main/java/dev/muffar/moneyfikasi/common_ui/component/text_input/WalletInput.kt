package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.WalletPickerSheet
import dev.muffar.moneyfikasi.common_ui.component.icon.BoxedIcon
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
        CommonTextInput(
            modifier = Modifier.fillMaxWidth(),
            value = wallet.name,
            onValueChange = {},
            onClear = onClear,
            label = label,
            isClickable = true,
            error = error,
            onClick = { showWalletPicker = true },
            leadingIcon = {
                BoxedIcon(
                    icon = wallet.icon,
                    color = wallet.color
                )
            }
        )
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
