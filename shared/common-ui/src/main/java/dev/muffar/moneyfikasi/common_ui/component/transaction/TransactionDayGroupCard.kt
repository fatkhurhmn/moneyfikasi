package dev.muffar.moneyfikasi.common_ui.component.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.GroupTransactionHeader
import dev.muffar.moneyfikasi.common_ui.component.transaction.item.TransactionItem
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.resource.R
import org.threeten.bp.LocalDateTime
import java.util.UUID

@Composable
fun TransactionDayGroupCard(
    date: LocalDateTime,
    balance: Double,
    transactions: List<Transaction>,
    onTransactionClick: (UUID, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        GroupTransactionHeader(date = date, balanceOnDate = balance)
        CommonHorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f)
        )
        if (transactions.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_transactions_msg),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        } else {
            Column {
                transactions.forEachIndexed { index, tx ->
                    TransactionItem(
                        transaction = tx,
                        onClick = { onTransactionClick(tx.id, tx.isTransfer) }
                    )
                    if (index < transactions.lastIndex) {
                        CommonHorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}
