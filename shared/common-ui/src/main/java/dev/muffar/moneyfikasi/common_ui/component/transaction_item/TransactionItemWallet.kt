package dev.muffar.moneyfikasi.common_ui.component.transaction_item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.Wallet

@Composable
fun TransactionItemWallet(wallet: Wallet) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(wallet.color).copy(alpha = 0.1f),
        ),
        border = BorderStroke(0.5f.dp, Color(wallet.color)),
        shape = RoundedCornerShape(4.dp),
    ) {
        Text(
            text = wallet.name,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(wallet.color),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
        )
    }
}