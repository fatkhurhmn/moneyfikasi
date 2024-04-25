package dev.muffar.moneyfikasi.statistic.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.GroupTransactionHeader
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.statistic.detail.component.StatisticDetailItem
import dev.muffar.moneyfikasi.utils.format
import dev.muffar.moneyfikasi.utils.formatThousand

@Composable
fun StatisticDetailScreen(
    modifier: Modifier = Modifier,
    state: StatisticDetailState,
    onBackClick: () -> Unit,
) {
    val transactionsByDate = state.transactions.groupBy { it.date.format("yyyy-MM-dd") }
    val dates = transactionsByDate.keys.toList()
    val transactions = transactionsByDate.values.toList()

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.statistic_detail),
                onBackClick = onBackClick
            )
        }
    ) {
        LazyColumn(
            modifier = modifier.padding(it)
        ) {
            item {
                val total = state.transactions.sumOf { i -> i.amount }
                val prefix = if (state.type == TransactionType.INCOME) "+" else "-"
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
                        text = "$prefix${total.toLong().formatThousand()}",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                        color = color
                    )
                }
            }

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
                    StatisticDetailItem(transaction = transaction)
                }

                item {
                    HorizontalDivider(
                        thickness = 8.dp,
                        color = MaterialTheme.colorScheme.outline.copy(0.08f)
                    )
                }
            }
        }
    }
}