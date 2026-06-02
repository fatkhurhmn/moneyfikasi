package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.WalletPickerSheet
import dev.muffar.moneyfikasi.common_ui.component.icon.BoxedIcon
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.constants.UUIDConst

@Composable
fun WalletInput(
    wallet: Wallet,
    error: ErrorMessage = ErrorMessage(),
    label: String = stringResource(R.string.label_wallet),
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
                val iconColor = if (wallet.id == UUIDConst.empty) {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(0.7f).toArgb().toLong()
                } else {
                    wallet.color
                }
                BoxedIcon(
                    icon = wallet.icon,
                    color = iconColor
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
