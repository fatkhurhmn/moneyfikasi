package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
fun TransactionOverviewSection(
    modifier: Modifier = Modifier,
    income: Double,
    expense: Double,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                val formattedIncome = income.toLong().formatThousand().let {
                    if (income > 0) "+$it" else it
                }
                Text(
                    text = stringResource(R.string.income),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = formattedIncome,
                    style = MaterialTheme.typography.titleMedium,
                    color = MainColor.Green.primary
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                val formattedExpense = expense.toLong().formatThousand().let {
                    if (expense > 0) "-$it" else it
                }
                Text(
                    text = stringResource(R.string.expense),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = formattedExpense,
                    style = MaterialTheme.typography.titleMedium,
                    color = MainColor.Red.primary
                )
            }
        }
        HorizontalDivider(
            thickness = 8.dp,
            color = MaterialTheme.colorScheme.outline.copy(0.08f)
        )
    }
}