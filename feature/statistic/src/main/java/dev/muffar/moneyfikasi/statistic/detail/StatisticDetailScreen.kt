package dev.muffar.moneyfikasi.statistic.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.GroupTransactionHeaderV2
import dev.muffar.moneyfikasi.common_ui.component.transaction_item.TransactionItem
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.format
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime
import java.util.UUID

@Composable
fun StatisticDetailScreen(
    modifier: Modifier = Modifier,
    categoryName: String,
    state: StatisticDetailState,
    onClick: (UUID, Boolean) -> Unit,
    onBackClick: () -> Unit,
    onGetDailySum: (LocalDateTime) -> Flow<Double>,
) {
    val transactions = state.transactions.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = categoryName,
                onBackClick = onBackClick
            )
        }
    ) {
        LazyColumn(
            modifier = modifier.padding(it)
        ) {
            item {
                val total = state.totalAmount.formatThousand()

                val color =
                    if (state.type == TransactionType.INCOME) MainColor.Green.primary else MainColor.Red.primary

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.total),
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                    )
                    Text(
                        text = if (state.type == TransactionType.INCOME) "+$total" else "-$total",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                        color = color
                    )
                }
            }

            items(
                count = transactions.itemCount,
                key = transactions.itemKey { transaction -> transaction.id }
            ) { index ->
                val transaction = transactions[index] ?: return@items
                val prevTransaction = if (index > 0) transactions[index - 1] else null

                val isNewDay = prevTransaction == null ||
                        (transaction.date.format("yyyy-MM-dd") != prevTransaction.date.format("yyyy-MM-dd"))

                if (isNewDay) {
                    val dailySumFlow = remember(transaction.date.toLocalDate()) {
                        onGetDailySum(transaction.date)
                    }
                    val balance by dailySumFlow.collectAsState(initial = 0.0)
                    GroupTransactionHeaderV2(date = transaction.date, balanceOnDate = balance)
                    CommonHorizontalDivider()
                }

                TransactionItem(
                    transaction = transaction,
                    onClick = { id ->
                        onClick(
                            id,
                            transaction.isTransfer || transaction.category.isFeeTransfer
                        )
                    }
                )

                val nextTransaction =
                    if (index < transactions.itemCount - 1) transactions[index + 1] else null
                val isEndOfDay = nextTransaction == null ||
                        transaction.date.format("yyyy-MM-dd") != nextTransaction.date.format("yyyy-MM-dd")

                CommonHorizontalDivider(if (isEndOfDay) 8.dp else 0.dp)
            }

            when (transactions.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .wrapContentSize(Alignment.Center)
                        )
                    }
                }

                else -> {}
            }
        }
    }
}
