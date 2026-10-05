package dev.muffar.moneyfikasi.common_ui.component.transaction

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.formattedDate
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime
import java.util.UUID

@Composable
fun TransactionsList(
    modifier: Modifier = Modifier,
    transactions: LazyPagingItems<Transaction>,
    onItemClick: (UUID, Boolean) -> Unit,
    onGetDailyBalance: (LocalDateTime) -> Flow<Double>,
    extraBottomSpace: Boolean = false,
    header: @Composable () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            bottom = if (extraBottomSpace) 100.dp else 16.dp,
            top = 8.dp,
            start = 16.dp,
            end = 16.dp
        ),
    ) {
        item {
            header()
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(
            count = transactions.itemCount,
            key = transactions.itemKey { it.id }
        ) { index ->
            val transaction = transactions[index] ?: return@items
            val prevTransaction = if (index > 0) transactions[index - 1] else null

            val isNewDay = prevTransaction == null ||
                    transaction.date.formattedDate() != prevTransaction.date.formattedDate()

            // Only render one card per day group (when isNewDay), collect day's transactions
            if (isNewDay) {
                val dayTransactions = mutableListOf<Transaction>()
                var i = index
                while (i < transactions.itemCount) {
                    val tx = transactions[i] ?: break
                    if (tx.date.formattedDate() != transaction.date.formattedDate()) break
                    dayTransactions.add(tx)
                    i++
                }
                val dailyBalanceFlow = remember(transaction.date.toLocalDate()) {
                    onGetDailyBalance(transaction.date)
                }
                val balance by dailyBalanceFlow.collectAsState(initial = 0.0)
                TransactionDayGroupCard(
                    date = transaction.date,
                    balance = balance,
                    transactions = dayTransactions,
                    onTransactionClick = onItemClick
                )
                Spacer(modifier = Modifier.height(8.dp))
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
