package dev.muffar.moneyfikasi.search.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.GroupTransactionHeader
import dev.muffar.moneyfikasi.common_ui.component.TransactionItem
import dev.muffar.moneyfikasi.domain.model.Transaction
import java.util.UUID

@Composable
fun TransactionsList(
    modifier: Modifier = Modifier,
    dates: List<String>,
    transactions: List<List<Transaction>>,
    onItemClick: (UUID) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 54.dp)
    ) {
        dates.forEachIndexed { index, _ ->
            item {
                GroupTransactionHeader(
                    date = transactions[index].first().date,
                    transactions = transactions[index]
                )
            }

            item {
                CommonHorizontalDivider()
            }

            items(
                items = transactions[index],
                key = { transaction -> transaction.id }
            ) { transaction ->
                TransactionItem(
                    transaction = transaction,
                    onClick = onItemClick
                )
            }
            item {
                CommonHorizontalDivider(8.dp)
            }
        }
    }
}