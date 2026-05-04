package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.CommonFilterChip
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R

@Composable
fun FilterWalletSection(
    modifier: Modifier = Modifier,
    wallets: List<Wallet>,
    selectedWallets: Set<Wallet>,
    onSelectAll: (Set<Wallet>) -> Unit,
    onSelect: (Wallet) -> Unit
) {
    val allWalletsSelected = wallets.all { it in selectedWallets }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        FilterSheetLabel(label = stringResource(R.string.wallet))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CommonFilterChip(
                label = stringResource(R.string.all),
                selected = allWalletsSelected,
                onSelect = {
                    val wallets = if (allWalletsSelected) emptySet() else wallets.toSet()
                    onSelectAll(wallets)
                }
            )
            wallets.forEach {
                WalletFilterChip(
                    wallet = it,
                    isSelect = it in selectedWallets,
                    onSelect = onSelect
                )
            }
        }
    }
}