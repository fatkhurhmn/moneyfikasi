package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.icon.BoxedIcon
import dev.muffar.moneyfikasi.common_ui.component.modifier.dottedBorder
import dev.muffar.moneyfikasi.domain.model.AppIcon
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand

@Composable
fun WalletSection(
    wallets: List<Wallet>,
    totalBalance: Double,
    isBalanceVisible: Boolean,
    onAddWalletClick: () -> Unit,
) {
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
            item(key = "total_wallet") {
                WalletCard(
                    iconName = AppIcon.Wallet.name,
                    iconColor = MaterialTheme.colorScheme.onPrimaryContainer.toArgb().toLong(),
                    walletName = stringResource(R.string.label_total_balance),
                    balance = totalBalance,
                    isBalanceVisible = isBalanceVisible,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            }

            items(
                items = wallets,
                key = { wallet -> wallet.id }
            ) { wallet ->
                WalletCard(
                    iconName = wallet.icon,
                    iconColor = wallet.color,
                    walletName = wallet.name,
                    balance = wallet.balance,
                    isBalanceVisible = isBalanceVisible
                )
            }

            item(key = "add_wallet") {
                AddWalletCard(
                    modifier = Modifier.fillParentMaxHeight(),
                    onClick = onAddWalletClick
                )
            }
        }
    }
}

@Composable
private fun WalletCard(
    iconName: String,
    iconColor: Long,
    walletName: String,
    balance: Double,
    isBalanceVisible: Boolean,
    containerColor: Color = MaterialTheme.colorScheme.surface
) {
    PrimaryCard {
        Column(
            modifier = Modifier
                .background(containerColor)
                .width(160.dp)
                .defaultMinSize(minHeight = 110.dp)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            BoxedIcon(
                icon = iconName,
                color = iconColor,
                containerSize = 40.dp,
                iconSize = 24.dp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = walletName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = if (isBalanceVisible) {
                    balance.formatThousand()
                } else {
                    stringResource(R.string.label_invisible_balance)
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun AddWalletCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .width(160.dp)
            .height(110.dp)
            .clip(MaterialTheme.shapes.medium)
            .dottedBorder(cornerRadius = 12.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
    }
}
