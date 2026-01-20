package dev.muffar.moneyfikasi.transaction.add_edit.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.IconFieldButton
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R

@Composable
fun WalletPicker(
    wallet: Wallet,
    onWalletClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        CommonTextInput(
            modifier = Modifier.weight(1f),
            value = wallet.name,
            onValueChange = {},
            label = stringResource(R.string.wallet),
            placeholder = stringResource(R.string.select_wallet),
            isClickable = true,
            onClick = onWalletClick
        )
        Spacer(modifier = Modifier.width(16.dp))
        IconFieldButton(
            icon = wallet.icon,
            color = wallet.color,
            showLabel = false,
            onIconClick = onWalletClick
        )
    }
}