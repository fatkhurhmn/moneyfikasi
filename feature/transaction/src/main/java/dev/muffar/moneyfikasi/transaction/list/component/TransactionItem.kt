package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.IconByName
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.utils.formatThousand
import org.threeten.bp.format.DateTimeFormatter
import java.util.UUID

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onClick: (UUID) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(transaction.id) }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            TransactionItemIcon(transaction.category)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = transaction.category.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                TransactionItemWallet(transaction.wallet)
            }
        }
        Column(
            horizontalAlignment = Alignment.End,
        ) {
            val formattedAmount = transaction.amount.toLong().formatThousand().let {
                if (transaction.type == TransactionType.EXPENSE) "-$it" else "+$it"
            }
            Text(
                text = formattedAmount,
                style = MaterialTheme.typography.bodyLarge,
                color = if (transaction.type == TransactionType.EXPENSE) MainColor.Red.primary else MainColor.Green.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = transaction.date.format(DateTimeFormatter.ofPattern("H:mm")),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun TransactionItemIcon(category: Category) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(category.color),
        ),
    ) {
        IconByName(
            name = category.icon,
            tint = Color.White,
            modifier = Modifier
                .padding(8.dp)
                .size(26.dp)
        )
    }
}

@Composable
private fun TransactionItemWallet(wallet: Wallet) {
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