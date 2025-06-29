package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.TransactionPieChart
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.resource.R

@Composable
fun TransactionStatisticContent(
    modifier: Modifier = Modifier,
    transactions: List<Transaction>,
    onClick: (List<Transaction>) -> Unit,
) {
    val transactionByCategory = transactions
        .groupBy { it.category }
        .toList()
        .sortedByDescending { (_, value) ->
            value.sumOf { it.amount }
        }
        .toMap()

    val ctx = LocalContext.current

    if (transactionByCategory.isNotEmpty()) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                TransactionPieChart(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    valueColor = ctx.getColor(R.color.white),
                    transactions = transactions
                )
            }

            items(transactionByCategory.size) { index ->
                val item = transactionByCategory.keys.toList()[index]
                val amount = transactionByCategory.values.toList()[index].sumOf { it.amount }
                val percentage = amount / transactions.sumOf { it.amount }
                val quantity = transactions.count { it.category == item }

                StatisticTransactionItem(
                    modifier = Modifier
                        .clickable { onClick(transactionByCategory.values.toList()[index]) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    category = item,
                    amount = amount,
                    percentage = percentage,
                    quantity = quantity,
                )
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_no_transactions),
                contentDescription = stringResource(R.string.no_transactions),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                modifier = Modifier.size(100.dp)
            )
            Text(text = stringResource(R.string.no_transactions))
        }
    }
}