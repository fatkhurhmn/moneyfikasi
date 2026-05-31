package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.icon.BoxedIcon
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand

@Composable
fun WalletSection(
    wallets: List<Wallet>,
    isBalanceVisible: Boolean,
) {
    if (wallets.isEmpty()) return

    Column {
        DashboardLabel(
            label = stringResource(R.string.title_wallets),
            moreButton = false
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(
                items = wallets,
                key = { wallet -> wallet.id }
            ) { wallet ->
                WalletCard(
                    wallet = wallet,
                    isBalanceVisible = isBalanceVisible
                )
            }
        }
    }
}

@Composable
private fun WalletCard(
    wallet: Wallet,
    isBalanceVisible: Boolean,
) {
    PrimaryCard {
        Column(
            modifier = Modifier
                .width(150.dp)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            BoxedIcon(
                icon = wallet.icon,
                color = wallet.color,
                containerSize = 40.dp,
                iconSize = 24.dp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = wallet.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = if (isBalanceVisible) {
                    wallet.balance.formatThousand()
                } else {
                    stringResource(R.string.label_invisible_balance)
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
