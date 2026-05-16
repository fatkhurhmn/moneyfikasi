package dev.muffar.moneyfikasi.wallet.add_edit.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.button.CommonSwitch
import dev.muffar.moneyfikasi.resource.R

@Composable
fun WalletActivationButton(
    isActive: Boolean,
    onIsActiveChange: () -> Unit
) {
    CommonSwitch(
        isEnabled = isActive,
        onEnabledChange = {
            onIsActiveChange()
        },
        title = stringResource(R.string.activation),
        description = stringResource(R.string.disable_category)
    )
}