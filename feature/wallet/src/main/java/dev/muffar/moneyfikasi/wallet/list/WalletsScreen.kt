package dev.muffar.moneyfikasi.wallet.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.button.CommonAddButton
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.wallet.list.component.TotalBalance
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
                    TotalBalance(total = state.balance)
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
            EmptyDataList(
                painter = painterResource(id = R.drawable.ic_empty_wallet),
                title = stringResource(id = R.string.no_wallets),
                description = stringResource(id = R.string.no_wallets_message)
            )
        }
    }
}