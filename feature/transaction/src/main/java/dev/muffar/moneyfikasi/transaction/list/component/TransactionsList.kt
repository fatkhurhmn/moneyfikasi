package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.Transaction

@Composable
fun TransactionsList(
    modifier: Modifier = Modifier,
    dates: List<String>,
    transactions: List<List<Transaction>>,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        dates.forEachIndexed { index, _ ->
            item {
                GroupTransactionHeader(
                    date = transactions[index].first().date,
                    transactions = transactions[index]
                )
            }

            item {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(0.08f)
                )
            }

            items(
                items = transactions[index],
                key = { transaction -> transaction.id }
            ) { transaction ->
                TransactionItem(
                    transaction = transaction,
                    onClick = {}
                )
            }
            item {
                HorizontalDivider(
                    thickness = 16.dp,
                    color = MaterialTheme.colorScheme.outline.copy(0.08f)
                )
            }
        }
    }
}