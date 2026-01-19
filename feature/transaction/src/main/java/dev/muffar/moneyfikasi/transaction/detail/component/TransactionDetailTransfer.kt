package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.IconByName
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.formatThousand

@Composable
fun TransactionDetailTransfer(
    amount: Double?,
    sourceWallet: Wallet?,
    targetWallet: Wallet?,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            WalletItem(
                name = sourceWallet?.name ?: "",
                icon = sourceWallet?.icon ?: "",
                color = sourceWallet?.color ?: 0
            )
            Spacer(Modifier.width(16.dp))
            Image(
                painter = painterResource(R.drawable.ic_transfer),
                contentDescription = null,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(24.dp),
            )
            Spacer(Modifier.width(16.dp))
            WalletItem(
                name = targetWallet?.name ?: "",
                icon = targetWallet?.icon ?: "",
                color = targetWallet?.color ?: 0
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.amount),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = amount?.formatThousand() ?: "",
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun WalletItem(
    name: String,
    color: Long,
    icon: String
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .background(Color(color))

                .width(70.dp)
                .height(40.dp),
            contentAlignment = Alignment.Center,
        ) {
            IconByName(
                name = icon,
                tint = MaterialTheme.colorScheme.background
            )
        }
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium
        )
    }
}