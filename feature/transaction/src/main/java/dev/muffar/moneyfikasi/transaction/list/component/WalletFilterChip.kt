package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonFilterChip
import dev.muffar.moneyfikasi.common_ui.component.icon.IconByName
import dev.muffar.moneyfikasi.domain.model.Wallet

@Composable
fun WalletFilterChip(
    wallet: Wallet,
    isSelect: Boolean,
    onSelect: (Wallet) -> Unit,
) {
    CommonFilterChip(
        label = wallet.name,
        selected = isSelect,
        leadingIcon = {
            IconByName(
                name = wallet.icon,
                tint = if (isSelect) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier.size(16.dp)
            )
        },
        onSelect = { onSelect(wallet) },
    )
}