package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.IconByName
import dev.muffar.moneyfikasi.domain.model.Wallet

@Composable
fun WalletFilterItem(
    modifier: Modifier = Modifier,
    wallet: Wallet,
    isSelect: Boolean,
    onSelect: (Wallet) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect(wallet) }
            .padding(horizontal = 16.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color(wallet.color))
                    .padding(2.dp),
                contentAlignment = Alignment.Center
            ) {
                IconByName(
                    name = wallet.icon,
                    tint = MaterialTheme.colorScheme.background
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = wallet.name)
        }
        Checkbox(checked = isSelect, onCheckedChange = { onSelect(wallet) })
    }
}