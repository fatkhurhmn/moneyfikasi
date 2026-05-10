package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.TransactionPieChart
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.resource.R

@Composable
fun StatisticByCategoryContent(
    transactions: Map<Category, List<Transaction>>,
    onItemClick: (Category) -> Unit,
    onShowAllClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (transactions.isNotEmpty()) {
            TransactionPieChart(transactions)
            Column {
                transactions.onEachIndexed { index, _ ->
                    val item = transactions.keys.toList()[index]
                    val amount =
                        transactions.values.toList()[index].sumOf { it.amount }
                    val totalAmount = transactions.values.flatten().sumOf { it.amount }
                    val percentage = if (totalAmount > 0) amount / totalAmount else 0.0
                    val quantity = transactions[item]?.size ?: 0

                    StatisticByCategoryItem(
                        category = item,
                        amount = amount,
                        percentage = percentage,
                        quantity = quantity,
                        onClick = onItemClick
                    )
                }
            }

            Text(
                text = stringResource(R.string.see_all),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable(onClick = onShowAllClick)
                    .padding(vertical = 2.dp, horizontal = 4.dp)
            )
        } else {
            EmptyDataList(
                painter = painterResource(R.drawable.ic_empty_transactions),
                title = stringResource(R.string.no_transactions),
                description = stringResource(R.string.no_transactions_message)
            )
        }
    }
}