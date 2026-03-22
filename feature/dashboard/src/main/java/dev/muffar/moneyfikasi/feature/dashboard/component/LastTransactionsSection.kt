package dev.muffar.moneyfikasi.feature.dashboard.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.transaction_item.TransactionItem
import dev.muffar.moneyfikasi.domain.model.Transaction
import java.util.UUID

@Composable
fun LastTransactionsSection(
    transactions: List<Transaction>,
    onSeeAllTransactionsClick: () -> Unit,
    onTransactionClick: (UUID) -> Unit,
) {
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Last Transactions",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "See All",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onSeeAllTransactionsClick() }
            )
        }

        transactions.forEach {
            TransactionItem(
                transaction = it,
                onClick = onTransactionClick,
                showDate = true
            )
        }
    }
}