package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.formatThousand

@Composable
fun StatisticOverviewSection(
    income: Double,
    expense: Double,
    total: Double,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val formattedIncome = income.toLong().formatThousand().let { amount ->
                if (income > 0) "+$amount" else amount
            }

            Text(
                text = stringResource(R.string.income),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = formattedIncome,
                style = MaterialTheme.typography.labelLarge,
                color = MainColor.Green.primary
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val formattedExpense = expense.toLong().formatThousand().let { amount ->
                if (expense > 0) "+$amount" else amount
            }
            Text(
                text = stringResource(R.string.expense),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = formattedExpense,
                style = MaterialTheme.typography.labelLarge,
                color = MainColor.Red.primary
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.total),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = total.toLong().formatThousand(),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}