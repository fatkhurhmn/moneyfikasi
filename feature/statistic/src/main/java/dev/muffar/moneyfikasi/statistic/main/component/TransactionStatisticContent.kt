package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.IconByName
import dev.muffar.moneyfikasi.common_ui.component.TransactionPieChart
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.format
import dev.muffar.moneyfikasi.utils.formatThousand

@Composable
fun TransactionStatisticContent(
    modifier: Modifier = Modifier,
    transactions: List<Transaction>,
) {
    val transactionByCategory = transactions
        .groupBy { it.category }
        .toList()
        .sortedByDescending { (_, value) ->
            value.sumOf { it.amount }
        }
        .toMap()

    val ctx = LocalContext.current

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
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                category = item,
                amount = amount,
                percentage = percentage,
                quantity = quantity
            )
        }
    }
}

@Composable
fun StatisticTransactionItem(
    modifier: Modifier = Modifier,
    category: Category,
    amount: Double,
    percentage: Double,
    quantity: Int,
) {
    val formattedAmount = if (category.type == CategoryType.INCOME) {
        amount.toLong().formatThousand()
    } else {
        "-${amount.toLong().formatThousand()}"
    }

    val color =
        if (category.type == CategoryType.INCOME) MainColor.Green.primary else MainColor.Red.primary

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(Color(category.color))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                IconByName(name = category.icon, tint = MainColor.White)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${(percentage * 100).format(2)}%",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = formattedAmount,
                color = color,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.qty_transactions, quantity),
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}