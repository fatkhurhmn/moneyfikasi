package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.icon.LabeledIcon
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand

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
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (sourceWallet != null) {
                LabeledIcon(
                    icon = sourceWallet.icon,
                    label = sourceWallet.name,
                    color = sourceWallet.color,
                    isLabelPrefix = true,
                    fill = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.45f)
                )
            }

            Box(
                modifier = Modifier.weight(0.1f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }

            if (targetWallet != null) {
                LabeledIcon(
                    icon = targetWallet.icon,
                    label = targetWallet.name,
                    color = targetWallet.color,
                    isLabelPrefix = false,
                    fill = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.45f)
                )
            }

        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.label_amount),
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