package dev.muffar.moneyfikasi.common_ui.component.transaction_item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.icon.BoxedIcon
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import org.threeten.bp.format.DateTimeFormatter
import java.util.UUID

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onClick: (UUID) -> Unit,
    showDate: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(transaction.id) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            val date = if (showDate) {
                transaction.date.format(DateTimeFormatter.ofPattern("dd MMM, H:mm"))
            } else {
                transaction.date.format(DateTimeFormatter.ofPattern("H:mm"))
            }
            BoxedIcon(
                icon = transaction.category.icon,
                color = transaction.category.color
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = transaction.category.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.End,
        ) {
            val financeColors = MoneyfikasiTheme.financeColors
            Text(
                text = getFormattedAmount(transaction.amount, transaction.type),
                style = MaterialTheme.typography.bodyLarge,
                color = getAmountColor(transaction.type, financeColors),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            ItemWalletCard(transaction.wallet)
        }
    }
}

private fun getFormattedAmount(amount: Double, type: TransactionType): String {
    return when (type) {
        TransactionType.INCOME, TransactionType.TRANSFER_IN -> "+${amount.formatThousand()}"
        TransactionType.EXPENSE, TransactionType.TRANSFER_OUT -> "-${amount.formatThousand()}"
    }
}

private fun getAmountColor(type: TransactionType, financeColors: dev.muffar.moneyfikasi.common_ui.theme.color.FinanceColors): Color {
    return when (type) {
        TransactionType.INCOME, TransactionType.TRANSFER_IN -> financeColors.income
        TransactionType.EXPENSE, TransactionType.TRANSFER_OUT -> financeColors.expense
    }
}
