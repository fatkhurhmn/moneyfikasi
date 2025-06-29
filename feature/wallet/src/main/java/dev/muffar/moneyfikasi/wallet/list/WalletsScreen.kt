package dev.muffar.moneyfikasi.wallet.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    val total = state.wallets.sumOf { i -> i.balance }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.total_n_balance),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            text = total.toLong().formatThousand(),
                            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                        )
                    }
                    Spacer(modifier = Modifier.size(16.dp))
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
                    painter = painterResource(id = R.drawable.ic_wallet),
                    contentDescription = stringResource(R.string.no_wallets),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    modifier = Modifier.size(100.dp)
                )
                Text(text = stringResource(R.string.no_wallets))
            }
        }
    }
}