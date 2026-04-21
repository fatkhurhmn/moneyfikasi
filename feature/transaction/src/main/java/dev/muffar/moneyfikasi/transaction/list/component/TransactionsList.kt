package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.GroupTransactionHeader
import dev.muffar.moneyfikasi.common_ui.component.transaction_item.TransactionItem
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.utils.extensions.format
import java.util.UUID

@Composable
fun TransactionsList(
    transactions: LazyPagingItems<Transaction>,
    onItemClick: (UUID, Boolean) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 54.dp)
    ) {
        for (index in 0 until transactions.itemCount) {
            val transaction = transactions[index] ?: continue
            val prevTransaction = if (index > 0) transactions[index - 1] else null

            val isNewDay = prevTransaction == null ||
                    transaction.date.format("yyyy-MM-dd") != prevTransaction.date.format("yyyy-MM-dd")

            if (isNewDay) {
                // This is a bit tricky with Paging as we don't have the full list to group by date
                // We'll calculate the group header on the fly
                val dateTransactions = mutableListOf<Transaction>()
                var nextIndex = index
                while (nextIndex < transactions.itemCount) {
                    val nextTx = transactions[nextIndex]
                    if (nextTx != null && nextTx.date.format("yyyy-MM-dd") == transaction.date.format("yyyy-MM-dd")) {
                        dateTransactions.add(nextTx)
                        nextIndex++
                    } else {
                        break
                    }
                }

                item {
                    GroupTransactionHeader(
                        date = transaction.date,
                        transactions = dateTransactions
                    )
                }

                item {
                    CommonHorizontalDivider()
                }
            }

            item(key = transaction.id) {
                TransactionItem(
                    transaction = transaction,
                    onClick = { id ->
                        onItemClick(
                            id,
                            transaction.isTransfer || transaction.category.isFeeTransfer
                        )
                    }
                )
            }

            val nextTransaction = if (index < transactions.itemCount - 1) transactions[index + 1] else null
            val isEndOfDay = nextTransaction == null ||
                    transaction.date.format("yyyy-MM-dd") != nextTransaction.date.format("yyyy-MM-dd")

            if (isEndOfDay) {
                item {
                    CommonHorizontalDivider(8.dp)
                }
            } else {
                item {
                    CommonHorizontalDivider()
                }
            }
        }

        when (transactions.loadState.append) {
            is LoadState.Loading -> {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

            else -> {}
        }
    }
}
