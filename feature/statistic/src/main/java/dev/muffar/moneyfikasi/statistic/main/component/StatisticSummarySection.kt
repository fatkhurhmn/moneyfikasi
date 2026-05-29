package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand

@Composable
fun StatisticSummarySection(
    net: Double,
    income: Double,
    expense: Double,
) {
    val financeColors = MoneyfikasiTheme.financeColors
    val netSum = net.formatThousand()
    val incomeSum = income.formatThousand().let { if (income > 0) "+$it" else it }
    val expenseSum = expense.formatThousand().let { if (expense > 0) "-$it" else it }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        StatisticSectionLabel(
            label = stringResource(R.string.label_summary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        SummaryItem(
            label = stringResource(R.string.label_net),
            amount = netSum,
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            SummaryItem(
                label = stringResource(R.string.label_income),
                amount = incomeSum,
                color = financeColors.income,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            SummaryItem(
                label = stringResource(R.string.label_expense),
                amount = expenseSum,
                color = financeColors.expense,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
