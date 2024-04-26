package dev.muffar.moneyfikasi.wallet.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.CommonAddButton
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.formatThousand
import dev.muffar.moneyfikasi.wallet.list.component.WalletItem
import java.util.UUID

@Composable
fun WalletsScreen(
    modifier: Modifier = Modifier,
    state: WalletsState,
    onAddWalletClick: () -> Unit,
    onWalletItemClick: (UUID) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.wallets),
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            CommonAddButton(
                onClick = onAddWalletClick
            )
        }
    ) {
        if (state.wallets.isNotEmpty()) {
            LazyColumn(
                modifier = modifier.padding(it),
            ) {
                item {
                    val total = state.wallets.sumOf { i -> i.balance }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.total),
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                        )
                        Text(
                            text = total.toLong().formatThousand(),
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                        )
                    }
                }

                items(
                    items = state.wallets,
                    key = { wallet -> wallet.id }
                ) { wallet ->
                    WalletItem(
                        wallet = wallet,
                        onClick = { onWalletItemClick(wallet.id) }
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.TwoTone.AccountBalanceWallet,
                    contentDescription = stringResource(R.string.no_wallets),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    modifier = Modifier.size(100.dp)
                )
                Text(text = stringResource(R.string.no_wallets))
            }
        }
    }
}