package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Category
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R

@Composable
fun WalletsFilterTab(
    modifier: Modifier = Modifier,
    wallets: List<Wallet>,
    selectedWallets : Set<Wallet>,
    onSelectAll : (Boolean) -> Unit,
    onSelect : (Wallet) -> Unit
) {

    val isSelectAll = wallets.size == selectedWallets.size

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectAll(isSelectAll) }
                .padding(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.select_all),
                style = MaterialTheme.typography.titleMedium
            )
            Checkbox(
                checked = wallets.size == selectedWallets.size,
                onCheckedChange = { onSelectAll(isSelectAll) }
            )
        }
        if (wallets.isNotEmpty()) {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(wallets.size) {
                    val wallet = wallets[it]
                    WalletFilterItem(
                        wallet = wallet,
                        isSelect = wallet in selectedWallets,
                        onSelect = onSelect
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.TwoTone.Category,
                    contentDescription = stringResource(R.string.no_categories),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    modifier = Modifier.size(100.dp)
                )
                Text(text = stringResource(R.string.no_categories))
            }
        }
    }
}