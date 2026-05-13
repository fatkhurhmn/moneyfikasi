package dev.muffar.moneyfikasi.common_ui.component.transaction

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.GroupTransactionHeader
import dev.muffar.moneyfikasi.common_ui.component.transaction.item.TransactionItem
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.format
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime
import java.util.UUID

@Composable
fun TransactionsList(
    modifier: Modifier = Modifier,
    transactions: LazyPagingItems<Transaction>,
    onItemClick: (UUID, Boolean) -> Unit,
    onGetDailyBalance: (LocalDateTime) -> Flow<Double>,
    header: @Composable () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 100.dp),
    ) {
        item {
            header()
            HorizontalDivider(
                thickness = 8.dp,
                color = MaterialTheme.colorScheme.background
            )
        }
        items(
            count = transactions.itemCount,
            key = transactions.itemKey { it.id }
        ) { index ->
            val transaction = transactions[index] ?: return@items
            val prevTransaction = if (index > 0) transactions[index - 1] else null

            val isNewDay = prevTransaction == null ||
                    transaction.date.format("yyyy-MM-dd") != prevTransaction.date.format("yyyy-MM-dd")

            if (isNewDay) {
                val dailyBalanceFlow = remember(transaction.date.toLocalDate()) {
                    onGetDailyBalance(transaction.date)
                }
                val balance by dailyBalanceFlow.collectAsState(initial = 0.0)
                GroupTransactionHeader(date = transaction.date, balance)
                CommonHorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }

            TransactionItem(
                transaction = transaction,
                onClick = { id ->
                    onItemClick(id, transaction.isTransfer || transaction.category.isFeeTransfer)
                }
            )

            val nextTransaction =
                if (index < transactions.itemCount - 1) transactions[index + 1] else null
            val isEndOfDay = nextTransaction == null ||
                    transaction.date.format("yyyy-MM-dd") != nextTransaction.date.format("yyyy-MM-dd")

            if (isEndOfDay) {
                HorizontalDivider(
                    thickness = 8.dp,
                    color = MaterialTheme.colorScheme.background
                )
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
