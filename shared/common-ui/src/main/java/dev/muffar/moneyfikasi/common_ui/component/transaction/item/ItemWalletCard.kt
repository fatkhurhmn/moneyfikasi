package dev.muffar.moneyfikasi.common_ui.component.transaction.item

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.Wallet

@Composable
fun ItemWalletCard(
    wallet: Wallet,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(wallet.color).copy(alpha = 0.1f),
        ),
        shape = RoundedCornerShape(4.dp),
    ) {
        Text(
            text = wallet.name,
            style = textStyle,
            color = Color(wallet.color),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
        )
    }
}